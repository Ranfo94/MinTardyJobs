package Entity;

public class Job {

    private int ID;
    private int processingTime;
    private int dueDate;
    private int releaseDate;
    private int remainingTime;
    private int completeTime;


    public Job(int ID, int processingTime, int dueDate, int releaseDate) {
        this.ID = ID;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.releaseDate = releaseDate;
        this.remainingTime = processingTime;

    }

    public int getID() {
        return ID;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRemainingTime() {return remainingTime; }

    public void setRemainingTime(int remainingTime) {this.remainingTime = remainingTime;}

    public int getCompleteTime() { return completeTime; }

    public void setCompleteTime(int completeTime) { this.completeTime = completeTime; }
}
