/*

 */
package aimtrainer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * zpracovani fxml souboru na instanci tridy CustomMap
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
    private CustomMap cm;

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ziskani informaci o mape ze souboru fxml
        approachTime = (int) (2000.0 / Double.parseDouble((String) pane.getProperties().get("approachTime")));
        speed = Double.parseDouble((String) pane.getProperties().get("speed"));
        
        //defaultni velikost pro terce, ktere nemaji preddefinovanou vlastni velikost
        size = (Double.parseDouble((String) pane.getProperties().get("size")) * 10) + 10;
        
        colors = getColors(((String) pane.getProperties().get("colors")).split(","));
        MapSettings settings = new MapSettings(approachTime, time, speed, size, colors);
        children = pane.getChildren();
        
        cm = new CustomMap(settings, children);
    }
    
    public CustomMap getCustomMap(){
        return cm;
    }
    
    private List<Color> getColors(String[] list) {
        //text na barvu
        List<Color> selected = new ArrayList<>();
        for(int i = 0; i < list.length; i++){
            switch(list[i].toLowerCase()){
                case("red"):
                    selected.add(Color.CRIMSON);
                    break;
                case("green"):
                    selected.add(Color.LIMEGREEN);
                    break;
                case("blue"):
                    selected.add(Color.DODGERBLUE);
                    break;
                case("yellow"):
                    selected.add(Color.YELLOW);
                    break;
                case("violet"):
                    selected.add(Color.BLUEVIOLET);
                    break;
                case("gray"):
                    selected.add(Color.DARKGRAY);
                    break;
                case("dark gray"):
                    selected.add(Color.GRAY);
                    break;
                case("orange"):
                    selected.add(Color.ORANGE);
                    break;
                case("brown"):
                    selected.add(Color.SIENNA);
                    break;
                default:
                    try {
                        Color c = Color.valueOf(list[i]);
                        selected.add(c);
                    }catch (IllegalArgumentException ex){
                        //debug
                        System.out.println(list[i]);
                        Logger.getLogger(AimTrainer.class.getName()).log(Level.SEVERE, null, ex); //otestovat
                    }
            }
        }
        return selected;
    }
    
    
    
}
