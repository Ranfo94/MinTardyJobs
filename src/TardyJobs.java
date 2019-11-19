import Entity.Job;
import FileReader.CSVReader;
import FileWriter.FileWriter;
import JobSorter.JobSorter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TardyJobs {

    private static int max_iterations = 3000; //numero di iterazioni massime
    private static int num_movable = 4; //numero di job movable massimi
    private static int max_size_tardy = 10; //numero ottimo di tardy jobs
    private static List<Job> tardy_jobs;
    private static List<Job> schedule;
    private static int time = 0;

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();

        //PRENDO LA LISTA DI JOB DAL FILE CSV E ORDINO IN BASE ALLA DUE DATE
        List<Job> joblist = new CSVReader().getJobs();


        int iteration = 0;
        while (iteration<max_iterations){
            System.out.println("Starting iteration n°"+iteration);
            int lateJobs = 0;
            schedule = new ArrayList<>();
            tardy_jobs = new ArrayList<>();

            //controlla la prima schedula
            List<Job> m_list = createMovableList(joblist);
            List<Job> um_list = createUnmovableList(joblist);
            List<Job> sorted_mov_list = sortByDueDate(m_list);

            System.out.println("********** printing mov list **********");
            printJoblist(m_list);
            System.out.println("********** printing unmov list **********");
            printJoblist(um_list);

            if (compute_schedule(sorted_mov_list,um_list)){
                //controlla se la schedule trova è ammissibile
                /*for (Job job : tardy_jobs){
                    lateJobs++;
                }*/
                lateJobs = tardy_jobs.size();
                if (max_size_tardy >= lateJobs){
                    //ho trovato una schedula ammissibile

                    long endTime = System.currentTimeMillis();
                    System.out.println("********** printing schedule **********");
                    printJoblist(schedule);
                    System.out.println("i tardy jobs sono: " + lateJobs);
                    System.out.println("********** printing tardy jobs **********");
                    printJoblist(tardy_jobs);
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


    private static boolean compute_schedule(List<Job> sorted_mov_list,List<Job> um_list){
        for (int index =0;index<um_list.size();){
            Job um_job = um_list.get(index);
            if(um_job.getReleaseDate()<=time){
                //il job è stato rilasciato --> esegui
                time += um_job.getProcessingTime(); //avanzo il tempo
                if (time>um_job.getDueDate()){
                    //il job è in ritardo
                    tardy_jobs.add(um_job);
                    //um_job.setLate(true);
                }
                schedule.add(um_job);
                index++;
            }else {
                //il job non è eseguibile --> provo ad eseguire un movable job
                if (!search_for_executable_movable(sorted_mov_list)){
                    return false;
                }
            }
        }
        for (int j=0;j<sorted_mov_list.size();j++){
            Job job = sorted_mov_list.get(j);
            if (!schedule.contains(job)){
                //esegui
                time += job.getProcessingTime(); //avanzo il tempo
                if (time>job.getDueDate()){
                    //il job è in ritardo
                    //job.setLate(true);
                    tardy_jobs.add(job);
                }
                schedule.add(job);
            }
        }
        return true;
    }

    private static List<Job> sortByDueDate(List<Job> ns_list){
        List<Job> s_list = new ArrayList<Job>();
        int list_len = ns_list.size();
        s_list = new JobSorter().quickSort(ns_list,0,list_len-1);
        return s_list;
    }

    private static void printJoblist(List<Job> list){
        for ( Job job : list){
            System.out.println("ID: "+job.getID() + " PROCESSING TIME: "+ job.getProcessingTime()+ " DUE DATE: "+ job.getDueDate()
                    +" RELEASE DATE: "+job.getReleaseDate()
                    +" COMPLETE TIME: "+job.getCompleteTime()
                    +" COUNTER: "+job.getCounter());
        }
    }

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
        /*while (nmov>0){
            int job_index = ThreadLocalRandom.current().nextInt(0, list.size() - 1);
            Job job = list.get(job_index);
            if (!movable_list.contains(job)) {
                job.setMovable(true);
                job.advanceCounter();
                movable_list.add(job);
                --nmov;
            }
        }*/
        return movable_list;
    }

    private static Long discoverMinUsage(List<Job> jobList){
        Long min = jobList.get(0).getCounter();
        for (Job job: jobList){
            if (job.getCounter()<min)
                min = job.getCounter();
        }
        return min;
    }

    private static List<Job> createUnmovableList(List<Job> list){
        List<Job> unmovable_list = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            if(!list.get(i).isMovable()) {
                unmovable_list.add(list.get(i));
            }
        }
        return unmovable_list;
    }

    private static boolean search_for_executable_movable(List<Job> jobList){
        for (int i=0;i<jobList.size();i++){
            Job m_job = jobList.get(i);
            if (m_job.getReleaseDate()<=time && !schedule.contains(m_job)){
                //processo subito il job
                time+=m_job.getProcessingTime();
                if (time>m_job.getDueDate()){
                    //il job è in ritardo
                    //m_job.setLate(true);
                    tardy_jobs.add(m_job);
                }
                schedule.add(m_job);
                return true;
            }
        }
        return false;
    }

    /*
    public static List<Job> insertByDueDate (List<Job> movable_list , List<Job> unmovable_list){
        List<Job> list = new ArrayList<Job>();
        list = unmovable_list;
        //itero per tutti i jobs nella movable list
        for (int i=0;i<movable_list.size();i++){
            int proc_time = 0;
            int s = movable_list.get(i).getDueDate() - movable_list.get(i).getProcessingTime();
            //itero per tutti i job della unmovable list
            for (int j = 0; j<list.size();j++){
                if (j==list.size()-1){
                    list.add(j+1,movable_list.get(i));
                    List<Job> newTardy = createTardyList(unmovable_list);
                    if (newTardy.size() > tardy_jobs.size()){ //nuovo job in ritardo
                        tardy_jobs = newTardy; //aggiorno la lista dei jobs in ritardo
                        Job end_list = getMovJobWithHigherProcTime (unmovable_list);
                        list.remove(end_list);
                        list.add(list.size(),end_list);
                    }
                    break;
                }
                proc_time += list.get(j).getProcessingTime();
                int next_proc_time = list.get(j+1).getProcessingTime();
                int proc_time_sum = proc_time + next_proc_time;
                if (proc_time_sum> s){
                    list.add(j+1,movable_list.get(i));
                    List<Job> newTardy = createTardyList(unmovable_list);
                    if (newTardy.size() > tardy_jobs.size()){ //nuovo job in ritardo
                        tardy_jobs = newTardy; //aggiorno la lista dei jobs in ritardo
                        Job end_list = getMovJobWithHigherProcTime (unmovable_list);
                        list.remove(end_list);
                        list.add(list.size(),end_list);
                    }
                    break;
                }
            }
        }
        return list;
    }

    private static List<Job> createTardyList(List<Job> unmovable_list){
        List<Job> tardy_list = new ArrayList<Job>();
        int proc_time = 0;
        for (int i = 0;i<unmovable_list.size();i++){
            proc_time += unmovable_list.get(i).getProcessingTime();
            if (proc_time > unmovable_list.get(i).getDueDate()){
                //job i in ritardo
                tardy_list.add(unmovable_list.get(i));
            }
        }
        return tardy_list;
    }
    private static Job getMovJobWithHigherProcTime(List<Job> unmovable_list){
        Job max_job = null;
        int max_proc_time =0;
        for (int i=0;i<unmovable_list.size();++i){
            Job job = unmovable_list.get(i);
            if (job.isMovable()){
                if (job.getProcessingTime() > max_proc_time){
                    max_proc_time = job.getProcessingTime();
                    max_job = job;
                }
            }
        }
        return max_job;
    }*/

}

