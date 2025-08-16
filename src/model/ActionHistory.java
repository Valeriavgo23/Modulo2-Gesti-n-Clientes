package model;
public class ActionHistory {
    //Atrubutos
    private String description;
    private User performigUser;
    private long time;


    //Constructor
    /**
     * 
     * @param description descripción de la acción
     * @param performigUser Usuario que esta realizando la acción
     */
    public ActionHistory(String description, User performigUser){
        this.description = description;
        this.performigUser = performigUser;
        this.time = System.currentTimeMillis();

    }
    //getters
    public String getDescription() {
        return description;
    }


    public long getTime() {
        return time;
    }

    public User getPerformigUser() {
        return performigUser;
    }
    
    

    
}
