package model;
public enum Rol{
    //Declaramos constantes
    ADMINISTRATOR("Administrados"),
    STANDARD("Estandar");

    private String displayName;

    /**
     * 
     * @param displayName guarda el rol
     */
    Rol(String displayName){
        this.displayName = displayName;

    }
    //devuelve el nombre
    public String getDisplayName() {
        return displayName;
    }

    //Metodo 
    @Override
    public String toString(){
        return displayName;
    }
    
}