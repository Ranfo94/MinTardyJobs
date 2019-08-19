package FileWriter;

import Entity.Job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriter {

    public static void FileWriter(List<Job> tardyJobs, List<Job> orderedList, long runningTime) throws FileNotFoundException {

        PrintWriter printWriter = new PrintWriter(new File("Ordered.csv"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tardy ID");
        stringBuilder.append(';');
        stringBuilder.append("Tardy ProcessingTime");
        stringBuilder.append(';');
        stringBuilder.append("Tardy ReleaseDate");
        stringBuilder.append(';');
        stringBuilder.append("Tardy DueDate");
        stringBuilder.append('\n');
        stringBuilder.append("Num Tardy");
        stringBuilder.append(';');


        for (int i = 0; i < tardyJobs.size(); i++){
            Job job = tardyJobs.get(i);
            stringBuilder.append((job.getID()));
            stringBuilder.append(';');
            stringBuilder.append((job.getProcessingTime()));
            stringBuilder.append(';');
            stringBuilder.append(job.getReleaseDate());
            stringBuilder.append(';');
            stringBuilder.append(job.getDueDate());
            stringBuilder.append('\n');
        }

        stringBuilder.append(tardyJobs.size());
        stringBuilder.append(';');

        stringBuilder.append("Ordered ID");
        stringBuilder.append(';');
        stringBuilder.append("Ordered ProcessingTime");
        stringBuilder.append(';');
        stringBuilder.append("OrderedReleaseDate");
        stringBuilder.append(';');
        stringBuilder.append("Ordered DueDate");
        stringBuilder.append('\n');

        for (int i = 0; i < orderedList.size(); i++){
            Job job = orderedList.get(i);
            stringBuilder.append(job.getID());
            stringBuilder.append(';');
            stringBuilder.append(job.getProcessingTime());
            stringBuilder.append(';');
            stringBuilder.append(job.getReleaseDate());
            stringBuilder.append(';');
            stringBuilder.append(job.getDueDate());
            stringBuilder.append('\n');

            stringBuilder.append("Running Time");
            stringBuilder.append('\n');
            stringBuilder.append(runningTime);
            stringBuilder.append('\n');

            printWriter.write(stringBuilder.toString());
            printWriter.close();
        }

    }


}
