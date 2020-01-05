package FileReader;

import Entity.Job;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    //nome del file di input
    private String filename = "dataset/jobs15.csv";

    public List<Job> getJobs() throws FileNotFoundException {

        List<Job> jobs = new ArrayList<>();

        List<Integer> list_opt = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        try {

            //Salto la prima riga perché non contiene dati
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                int processingTime = Integer.parseInt(parts[1]);
                int dueTime = Integer.parseInt(parts[2]);
                int releaseTime = Integer.parseInt(parts[3]);
                boolean movable;
                Job job = new Job(id,processingTime,dueTime,releaseTime);
                jobs.add(job);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) reader.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return jobs;
    }
}
