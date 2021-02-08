/*

 */
package aimtrainer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author kubaj
 */
public class CustomMapController extends Thread implements Initializable {


    @FXML
    private Pane pane;
    ObservableList<Node> children;
   
    private Stage stage = AimTrainer.getPrimaryStage();
    private int approachTime;
    private int time;
    private double speed;
    private double size;
    private List<Color> colors = new ArrayList<Color>();

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        approachTime = (int) (1.0 / Integer.parseInt((String) pane.getProperties().get("approachTime")) * 3000);
        //time = Integer.parseInt((String) pane.getProperties().get("time"));
        speed = Double.parseDouble((String) pane.getProperties().get("speed"));
        size = (Double.parseDouble((String) pane.getProperties().get("size")) * 10) + 10;
        colors = getColors(((String) pane.getProperties().get("colors")).split(","));
        MapSettings settings = new MapSettings(approachTime, time, speed, size, colors);
        children = pane.getChildren();
        
        CustomMap cm = new CustomMap(settings, children);
        cm.start();
    }
    
    private List<Color> getColors(String[] list) {
        List<Color> selected = new ArrayList<>();
        for(int i = 0; i < list.length; i++){
            switch(list[i]){
                case("Červená"):
                    selected.add(Color.CRIMSON);
                    break;
                case("Zelená"):
                    selected.add(Color.LIMEGREEN);
                    break;
                case("Modrá"):
                    selected.add(Color.DODGERBLUE);
                    break;
                case("Žlutá"):
                    selected.add(Color.YELLOW);
                    break;
                case("Fialová"):
                    selected.add(Color.BLUEVIOLET);
                    break;
                case("Černá"):
                    selected.add(Color.BLACK);
                    break;
                case("Šedá"):
                    selected.add(Color.GRAY);
                    break;
                case("Oranžová"):
                    selected.add(Color.ORANGE);
                    break;
                case("Hnědá"):
                    selected.add(Color.SIENNA);
                    break;
            }
        }
        return selected;
    }
    
    
    
}
