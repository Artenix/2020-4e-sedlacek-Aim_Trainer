package aimtrainer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * trida, ktera predstavuje zaznam v seznamu vsech vlastnich map
 * @author kubaj
 */
public class CustomMapProp {
    
    private final StringProperty description;
    private String name;
    private String details;
    private CustomMap cm;
    
    public CustomMapProp(String name, String details, CustomMap cm) {
        this.name = name;
        this.details = details;
        this.description = new SimpleStringProperty(this, "description", name + "\n" + details);
        this.cm = cm;
    }
    
    public void startMap(){
        //zapnuti vlastni mapy a nastaveni jmena mapy ve tride CustomMap
        cm.setMapName(name);
        cm.start();
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }
    
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public final String getDescription() {
        return description.get();
    }

    public final void setDescription(String description) {
        this.description.set(description);
    }
    
    public final StringProperty descriptionProperty() {
        return description;
    }
}