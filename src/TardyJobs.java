import Entity.Job;
import FileReader.CSVReader;

import java.io.FileNotFoundException;
import java.util.List;

public class TardyJobs {

    public static void main(String[] args) throws FileNotFoundException {
        List<Job> joblist = new CSVReader().getJobs();
        printJoblist(joblist);
    }

    public static void printJoblist(List<Job> list){
        for ( Job job : list){
            System.out.println("ID: "+job.getID() + " PROCESSING TIME: "+ job.getProcessingTime()+ " DUE DATE: "+ job.getDueDate()
                    +" RELEASE DATE: "+job.getReleaseDate());
        }
    }
}

