package Entity;

/*
* Questa classe descrive come viene rappresentato un job all'interno del sistema ed implementa dei metodi per gestire e manipolare
* un generico job
*
* */

public class Job {

    private int ID; //identifictore univoco
    private int processingTime; //tempo di processamento
    private int dueDate; //due date
    private int releaseDate; //tempo di rilascio
    private boolean isMovable = false; //specifica se il job è movable
    //conta il numero di volte che un job è stato scelto come movable. Questo counter viene utilizzato per il criterio di scelta pseudocasuale
    private Long counter = 0L;

    /*Costuttore della classe Job*/
    public Job(int ID, int processingTime, int dueDate, int releaseDate) {
        this.ID = ID;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.releaseDate = releaseDate;
    }

    /*I seguenti metodi servono per accedere e per modificare gli attributi sopracitati per un job*/
    public int getID() {
        return ID;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getDueDate() {
        return dueDate;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) { isMovable = movable; }

    public Long getCounter() {
        return counter;
    }

    public void advanceCounter(){
        this.counter+=1;
    }
}
