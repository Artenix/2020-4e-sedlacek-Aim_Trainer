package aimtrainer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * trida predstavujici zaznamy v tabulce s vysledky
 * @author kubaj
 */
public class Highscore {
    
    private final IntegerProperty pos;
    private final StringProperty score;
    private String details;
    
    public Highscore(int pos, String score, String details) {
        this.pos = new SimpleIntegerProperty(this, "pos", pos);
        this.score = new SimpleStringProperty(this, "score", score);
        this.details = details;
    }
    
    

    public final int getPos() {
        return pos.get();
    }

    public final void setPos(int pos) {
        this.pos.set(pos);
    }

    public final IntegerProperty posProperty() {
        return pos;
    }

    public final String getScore() {
        return score.get();
    }

    public final void setScore(String score) {
        this.score.set(score);
    }

    public final StringProperty scoreProperty() {
        return score;
    }
    
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}