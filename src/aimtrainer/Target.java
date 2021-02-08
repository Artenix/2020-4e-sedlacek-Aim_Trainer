/*

 */
package aimtrainer;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;


/**
 *
 * @author kubaj
 */
public class Target {
    
    protected static int x;
    protected static int y;
    
    public Target() {
        Circle c = new Circle(x, y, 100);
        
    }
    
    public void setValues(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void genApproachCircle() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
