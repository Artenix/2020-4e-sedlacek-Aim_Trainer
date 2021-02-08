/*

 */
package aimtrainer;

import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author kubaj
 */
public class MapSettings {

    private int approachTime;
    private int time;
    private double speed;
    private double size;
    private List<Color> colors;
    
    public MapSettings(int approachTime, int time, double speed, double size, List colors) {
        this.approachTime = approachTime;
        this.time = time;
        this.speed = speed;
        this.size = size;
        this.colors = colors;
    }

    public int getApproachTime() {
        return approachTime;
    }

    public int getTime() {
        return time;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSize() {
        return size;
    }

    public List<Color> getColors() {
        return colors;
    }

    @Override
    public String toString() {
        return "MapSettings{" + "approachTime=" + approachTime + ", time=" + time + ", speed=" + speed + ", size=" + size + ", colors=" + colors + '}';
    }
    
    
}
