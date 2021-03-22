/*

 */
package aimtrainer;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * trida vytvarejici priblizovaci kruh
 * @author kubaj
 */
public class ApproachCircleGen {
    //parametry priblizovaciho kruhu
    protected double x;
    protected double y;
    protected double r;
    protected int approachTime;
    protected Color color;
    protected Circle circle;
    protected ScaleTransition st;
    protected FadeTransition ft;

    public ApproachCircleGen(double x, double y, double r, int approachTime, Color color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.approachTime = approachTime;
        this.color = color;
        circle = new Circle(this.x, this.y, this.r);
        circle.setStrokeWidth(r/30.0);
        circle.setFill(Color.TRANSPARENT);
        
        //zmensovani ("priblizovani") kruhu
        st = new ScaleTransition(Duration.millis(approachTime), circle);
        st.setFromX(0.7 + 160.0 / r);
        st.setFromY(0.7 + 160.0 / r);
        st.setToX(1.0);
        st.setToY(1.0);
        
        //objeveni se kruhu
        ft = new FadeTransition(Duration.millis(approachTime), circle);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
    }
    
    public Circle getCircle(){
        return circle;
    }
    
    public void startAnimation(){
        //spusteni vsech animaci
        ft.play();
        st.play();
        circle.setStroke(color);
        circle.fillProperty().set(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.2));      
    }
    
    public void stopAnimation(){
        //zastaveni animaci a zmizeni kruhu
        circle.setStroke(Color.TRANSPARENT);
        circle.setFill(Color.TRANSPARENT);
        ft.stop();
        st.stop();
    }
    
}
