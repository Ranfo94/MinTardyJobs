package FileWriter;

import Entity.Job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriter {

    public static void FileWriter(int numTardyJobs, List<Job> schedule, long runningTime) throws FileNotFoundException {

        PrintWriter printWriter = new PrintWriter(new File("output/out.csv"));
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Ordered Job ID");
        stringBuilder.append(';');
        stringBuilder.append("Ordered ProcessingTime");
        stringBuilder.append(';');
        stringBuilder.append("Ordered ReleaseDate");
        stringBuilder.append(';');
        stringBuilder.append("Ordered DueDate");
        stringBuilder.append('\n');

        for (int i = 0; i < schedule.size(); i++){
            Job job = schedule.get(i);
            stringBuilder.append(job.getID());
            stringBuilder.append(';');
            stringBuilder.append(job.getProcessingTime());
            stringBuilder.append(';');
            stringBuilder.append(job.getReleaseDate());
            stringBuilder.append(';');
            stringBuilder.append(job.getDueDate());
            stringBuilder.append('\n');


        }

        stringBuilder.append("Num Late Jobs");
        stringBuilder.append(';');
        stringBuilder.append("Running Time");
        stringBuilder.append('\n');
        stringBuilder.append(numTardyJobs);
        stringBuilder.append(';');
        stringBuilder.append(runningTime);
        stringBuilder.append('\n');

        stringBuilder.append("Late Job ID");
        stringBuilder.append('\n');
            for (Job job : schedule){
                if (job.isLate()){
                    stringBuilder.append(job.getID());
                    stringBuilder.append('\n');
                }
            }

        printWriter.write(stringBuilder.toString());
        printWriter.close();
    }


}
