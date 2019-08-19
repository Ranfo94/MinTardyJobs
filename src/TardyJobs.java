import Entity.Job;
import FileReader.CSVReader;
import JobSorter.JobSorter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TardyJobs {

    public static void main(String[] args) throws FileNotFoundException {

        //PRENDO LA LISTA DI JOB DAL FILE CSV E ORDINO IN BASE ALLA DUE DATE
        List<Job> joblist = new CSVReader().getJobs();
        List<Job> sorted = sortByDueDate(joblist);
        List<Job> schedule = new ArrayList<>();

        int iteration = 0;
        for (Job job : sorted){
            Job toExecute = getNextExecutable(sorted,iteration);
            if (toExecute !=null){
                //E' STATO SCELTO UN JOB, ESEGUI
                schedule.add(toExecute);
                //CONTROLLO CHE IL JOB ABBIA TERMINATO L'ESECUZIONE
                if (toExecute.getRemainingTime()>1){
                    //IL JOB NON E' ANCORA TERMINATO
                    toExecute.setRemainingTime(toExecute.getRemainingTime()-1);
                }else {
                    //IL JOB HA TERMINATO L'ESECUZIONE, TOGLILO DALLA LISTA
                    toExecute.setRemainingTime(toExecute.getRemainingTime()-1);
                    sorted.remove(toExecute);
                }
                iteration++;
            }else {
                //NON CI SONO JOB RILASCIATI DA ESEGUIRE, AUMENTA SOLO LE ITERAZIONI
                iteration++;
            }
        }
    }

    private static Job getNextExecutable(List<Job> list,int iteration){
        for (int i=0;i<list.size();++i){
            if(list.get(i).getReleaseDate()>= iteration){
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
                    +" RELEASE DATE: "+job.getReleaseDate());
        }
    }
}

