package Entity;

public class Job {

    private int ID;
    private int processingTime;
    private int dueDate;
    private int releaseDate;
    private int completeTime;
    private boolean isMovable = false;
    private Long counter = 0L;

    public Job(int ID, int processingTime, int dueDate, int releaseDate) {
        this.ID = ID;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.releaseDate = releaseDate;
    }

    public Job(int ID, int processingTime, int dueDate, int releaseDate, int remainingTime, int completeTime, boolean late) {
        this.ID = ID;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.releaseDate = releaseDate;
        this.completeTime = completeTime;
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

    public int getCompleteTime() { return completeTime; }

    public void setCompleteTime(int completeTime) { this.completeTime = completeTime; }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public void advanceCounter(){
        this.counter+=1;
    }
}
