import Entity.Job;
import FileReader.CSVReader;
import FileWriter.FileWriter;
import JobSorter.JobSorter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TardyJobs {

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();

        //PRENDO LA LISTA DI JOB DAL FILE CSV E ORDINO IN BASE ALLA DUE DATE
        List<Job> joblist = new CSVReader().getJobs();
        List<Job> sorted = sortByDueDate(joblist);
        List<Job> schedule = new ArrayList<>();
        int lateJobs = 0;

        int iteration = 0;
        while (sorted.size()!=0){
            Job toExecute = getNextExecutable(sorted,iteration);
            if (toExecute !=null){
                //E' STATO SCELTO UN JOB, ESEGUI
                iteration++;
                //CONTROLLO CHE IL JOB ABBIA TERMINATO L'ESECUZIONE
                if (toExecute.getRemainingTime()>1){
                    //IL JOB NON E' ANCORA TERMINATO
                    toExecute.setRemainingTime(toExecute.getRemainingTime()-1);
                }else {
                    //IL JOB HA TERMINATO L'ESECUZIONE, TOGLILO DALLA LISTA
                    toExecute.setRemainingTime(toExecute.getRemainingTime()-1);
                    toExecute.setCompleteTime(iteration);

                    //CONTROLLA SE IL JOB E' IN RITARDO
                    if (toExecute.getCompleteTime()>toExecute.getDueDate())
                        toExecute.setLate(true);
                    sorted.remove(toExecute);
                }
                if (schedule.size()==0 || schedule.get(schedule.size()-1).getID() != toExecute.getID())
                    schedule.add(new Job(toExecute.getID(),toExecute.getProcessingTime(),toExecute.getDueDate(),toExecute.getReleaseDate(),toExecute.getRemainingTime(),toExecute.getCompleteTime(),toExecute.isLate()));
                else {
                    schedule.remove(schedule.get(schedule.size()-1));
                    schedule.add(new Job(toExecute.getID(),toExecute.getProcessingTime(),toExecute.getDueDate(),toExecute.getReleaseDate(),toExecute.getRemainingTime(),toExecute.getCompleteTime(),toExecute.isLate()));
                }
            }else {
                //NON CI SONO JOB RILASCIATI DA ESEGUIRE, AUMENTA SOLO LE ITERAZIONI
                iteration++;
            }
        }

        for (Job job : schedule){
            if (job.isLate())
                lateJobs++;
        }

        System.out.println("LATE JOBS: "+lateJobs);

        long endTime = System.currentTimeMillis();

        printJoblist(schedule);


        FileWriter fileWriter = new FileWriter();
        fileWriter.FileWriter(lateJobs, schedule, endTime - startTime);
    }

    private static Job getNextExecutable(List<Job> list,int iteration){
        for (int i=0;i<list.size();++i){
            if(list.get(i).getReleaseDate()<= iteration){
                return list.get(i);
            }
        }
        return null;
    }

    private static List<Job> sortByDueDate(List<Job> ns_list){
        List<Job> s_list = new ArrayList<Job>();
        int list_len = ns_list.size();
        s_list = new JobSorter().quickSort(ns_list,0,list_len-1);
        return s_list;
    }

    public static void printJoblist(List<Job> list){
        for ( Job job : list){
            System.out.println("ID: "+job.getID() + " PROCESSING TIME: "+ job.getProcessingTime()+ " DUE DATE: "+ job.getDueDate()
                    +" RELEASE DATE: "+job.getReleaseDate()+" REMAINING TIME: "+job.getRemainingTime()
                    +" COMPLETE TIME: "+job.getCompleteTime()+" LATE: "+ job.isLate());
        }
    }
}

