import Entity.Job;
import FileReader.CSVReader;
import FileWriter.FileWriter;
import JobSorter.JobSorter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TardyJobs {

    private static int max_iterations = 10000; //Numero di iterazioni massime
    private static int num_movable = 3; //Numero di job movable massimi
    private static int max_size_tardy = 8; //Numero massimo di tardy jobs
    private static List<Job> tardy_jobs; //Lista dei jobs in ritardo
    private static List<Job> schedule; //Schedule finale
    private static int time = 0;

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();

        //Creazione di una lista di job a partire dal file CSV di input
        List<Job> joblist = new CSVReader().getJobs();

        int iteration = 0; //iterazione corrente
        while (iteration<max_iterations){
            System.out.println("Starting iteration n°"+iteration);
            int lateJobs = 0;
            schedule = new ArrayList<>();
            tardy_jobs = new ArrayList<>();

            //Crea la lista dei movable jobs
            List<Job> m_list = createMovableList(joblist);
            //Crea la lista degli unmovable jobs
            List<Job> um_list = createUnmovableList(joblist);
            //Ordina i movable jobs in base alla Due Date
            List<Job> sorted_mov_list = sortByDueDate(m_list);

            //Effettua la computazione sulle liste appena create
            if (compute_schedule(sorted_mov_list,um_list)){
                //controlla se la schedule trovata è ammissibile
                lateJobs = tardy_jobs.size();
                if (max_size_tardy >= lateJobs){
                    //ho trovato una schedula ammissibile
                    long endTime = System.currentTimeMillis();
                    System.out.println("********** printing schedule **********");
                    printJoblist(schedule);
                    System.out.println("i tardy jobs sono: " + lateJobs);
                    System.out.println("********** printing tardy jobs **********");
                    printJoblist(tardy_jobs);
                    //scrivi il risultato su un file CSV
                    FileWriter fileWriter = new FileWriter();
                    fileWriter.FileWriter(lateJobs, schedule, endTime - startTime,tardy_jobs);
                    return;
                }

            }

            for (Job job : joblist){
                job.setMovable(false);
            }
            System.out.println("****** printing late jobs*******");
            printJoblist(tardy_jobs);
            time=0;
            iteration++;
        }
    }


    /*
    * Esegue la computazione sulle liste di jobs create.
    * Si cerca di eseguire prima i job della unmovable list. Se il job che deve essere eseguito non è stato rilasciato, allora si cerca un
    * movable job che possa eseguire prima di lui. Se non ci sono job che possono essere eseguiti allora la schedula viene considerata come
    * 'non fattibile' e viene restituito il valore false.
    *
    * @param sorted_mov_list: lista di movable jobs ordinata per due date;
    * @param um_list: lista di unmovable jobs;
    * @out: true se la computazione è valida, altrimenti false
    * */
    private static boolean compute_schedule(List<Job> sorted_mov_list,List<Job> um_list){
        List<Job> delayed_movable = new ArrayList<>();
        for (int index =0;index<um_list.size();){
            Job um_job = um_list.get(index);
            if(um_job.getReleaseDate()<=time){
                //il job è stato rilasciato
                time += um_job.getProcessingTime(); //avanzo il tempo
                if (time>um_job.getDueDate()){
                    //il job è in ritardo
                    tardy_jobs.add(um_job);
                }
                //aggiungo il job alla schedule
                schedule.add(um_job);
                index++;
            }else {
                //il job non è eseguibile. Provo allora ad eseguire un movable job
                if (!search_for_executable_movable(sorted_mov_list)){
                    return false;
                }
            }
        }
        for (int j=0;j<sorted_mov_list.size();j++){
            Job job = sorted_mov_list.get(j);
            if (job.getReleaseDate()>time){
                //il job non è ancora stato rilasciato --> trova un movable rilasciato
                if (!search_for_executable_movable(sorted_mov_list)){
                    //non c'è un job eseguibile
                    return false;
                }
            }else{
                //il job è stato rilasciato
                if (!schedule.contains(job)){
                    //se il job è in ritardo lo mando in fondo alla lista dei movable per non ritardare anche i successivi
                    if (isJobLate(job) && !delayed_movable.contains(job)){
                        //manda il job in fondo alla lista dei movable e passa al successivo
                        sorted_mov_list.remove(job);
                        sorted_mov_list.add(sorted_mov_list.size(),job);
                        delayed_movable.add(job);
                    }else{
                        //esegui
                        time += job.getProcessingTime(); //avanzo il tempo
                        if (time>job.getDueDate()){
                            //il job è in ritardo
                            tardy_jobs.add(job);
                        }
                        schedule.add(job);
                    }
                }
            }
        }
        return true;
    }

    /*
    * Ordina una lista di jobs in base al valore della due date dei job. Per eseguire l'ordinamento viene applicato l'algoritmo di Quicksort.
    *
    * @param ns_list: lista non ordinata di jobs
    * @out: lista di jobs ordinata in base alle due date dei jobs
    * */
    private static List<Job> sortByDueDate(List<Job> ns_list){
        List<Job> s_list = new ArrayList<Job>();
        int list_len = ns_list.size();
        s_list = new JobSorter().quickSort(ns_list,0,list_len-1);
        return s_list;
    }

    /*
    * Crea una lista di Jobs movable a partire dalla lista di tutti i jobs. Utilizza un algoritmo pseudo casuale che tiene conto del numero di
    * occorrenze dei jobs nelle schedule precedenti.
    *
    * @param list: lista dei job totali
    * @out: lista dei movable jobs
    * */
    private static List<Job> createMovableList(List<Job> list){
        List<Job> movable_list = new ArrayList<Job>();
        int nmov = num_movable;
        Long minValue= 0L;
        while (nmov>0) {
            minValue = discoverMinUsage(list);
            List<Job> minList = new ArrayList<>();
            for (Job j : list) {
                if (j.getCounter().equals(minValue))
                    minList.add(j);
            }
            if (minList.size() > nmov) {
                while (nmov>0) {
                    int job_index = ThreadLocalRandom.current().nextInt(0, minList.size() - 1);
                    Job job = minList.get(job_index);
                    if (!movable_list.contains(job)) {
                        job.setMovable(true);
                        job.advanceCounter();
                        movable_list.add(job);
                        --nmov;
                    }
                }
                return movable_list;
            } else {
                for (Job j : minList) {
                    j.advanceCounter();
                    j.setMovable(true);
                    movable_list.add(j);
                    --nmov;
                }
            }
        }
        return movable_list;
    }

    /*
    * Crea una lista di jobs unmovable a partire dalla lista di tutti i jobs.
    *
    * @param list: lista dei job totali;
    * @out: lista di jobs unmovable.
    * */
    private static List<Job> createUnmovableList(List<Job> list){
        List<Job> unmovable_list = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            if(!list.get(i).isMovable()) {
                unmovable_list.add(list.get(i));
            }
        }
        return unmovable_list;
    }

    private static Long discoverMinUsage(List<Job> jobList){
        Long min = jobList.get(0).getCounter();
        for (Job job: jobList){
            if (job.getCounter()<min)
                min = job.getCounter();
        }
        return min;
    }

    /*
    * Cerca all'interno della lista dei movable jobs un job che possa essere immediatamente eseguito.
    *
    * @param jobList: lista dei movable jobs;
    * @out: se esista un job che possa essere eseguito subito restituisce true, altrimenti false
    * */
    private static boolean search_for_executable_movable(List<Job> jobList){
        for (int i=0;i<jobList.size();i++){
            Job m_job = jobList.get(i);
            if (m_job.getReleaseDate()<=time && !schedule.contains(m_job)){
                //processo subito il job
                time+=m_job.getProcessingTime();
                if (time>m_job.getDueDate()){
                    //il job è in ritardo
                    tardy_jobs.add(m_job);
                }
                //aggiungo il job alla lista
                schedule.add(m_job);
                return true;
            }
        }
        return false;
    }

    /*
    * Stampa a schermo una lista di jobs
    *
    * @param list: lista dei job da stampare
    * */
    private static void printJoblist(List<Job> list){
        for ( Job job : list){
            System.out.println("ID: "+job.getID() + " PROCESSING TIME: "+ job.getProcessingTime()+ " DUE DATE: "+ job.getDueDate()
                    +" RELEASE DATE: "+job.getReleaseDate()
                    +" COMPLETE TIME: "+job.getCompleteTime()
                    +" COUNTER: "+job.getCounter());
        }
    }

    /*
    * Semplice metodo che controlla se un job sarà in ritardo dopo la sua esecuzione.
    *
    * @param job: il job da controllare
    * @out: true se il job sarà in ritardo dopo l'esecuzione
    * */
    private static boolean isJobLate(Job job){
        if (time+job.getProcessingTime()>job.getDueDate()){
            return true;
        }
        return false;
    }

}

