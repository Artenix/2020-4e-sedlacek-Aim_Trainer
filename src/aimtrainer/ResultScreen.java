/*

 */
package aimtrainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author kubaj
 */
public class ResultScreen {
    private Stage stage = AimTrainer.getPrimaryStage();
    private FlowPane root;
    private Result res;
    private List score;
    private String path;
    private MapSettings settings;

    public ResultScreen(Result res, MapSettings settings) {
        this.settings = settings;
        this.res = res;
        path = "C:\\Users\\kubaj\\Desktop\\random_highscores.csv";
        score = new ArrayList<>();
        loadValues();
        if(res.getScore() > 0) {
            addResultToFile(res);
        }
        
        root = new FlowPane();
        root.setPadding(new Insets(150));
        GridPane grid = new GridPane();
        Font font = new Font("Futura", 60);
        Label lScore = new Label("Score: ");
        lScore.setFont(font);
        Text tScore = new Text(String.valueOf(res.getScore()));
        tScore.setFont(font);
        grid.add(lScore, 0, 0);
        grid.add(tScore, 1, 0);
        
        Label lHit = new Label("Hit: ");
        lHit.setFont(font);
        Text tHit = new Text(String.valueOf(res.getHit()));
        tHit.setFont(font); 
        grid.add(lHit, 0, 1);
        grid.add(tHit, 1, 1);
        
        Label lMiss = new Label("Missed: ");
        lMiss.setFont(font);
        Text tMiss = new Text(String.valueOf(res.getMissed()));
        tMiss.setFont(font);
        grid.add(lMiss, 0, 2);
        grid.add(tMiss, 1, 2);
        
        Label lCombo = new Label("Combo: ");
        lCombo.setFont(font);
        Text tCombo = new Text(String.valueOf(res.getHighestCombo()));
        tCombo.setFont(font);
        grid.add(lCombo, 0, 3);
        grid.add(tCombo, 1, 3);
        root.getChildren().add(grid);
        
        //premistit do bottom left rohu
        Button bBack = new Button("Back");
        bBack.setOnMouseClicked((event) -> {
            RandomMapSettings rms = new RandomMapSettings();
            stage.getScene().setRoot(rms.getRoot());
        });
        root.getChildren().add(bBack);
        
        Button bRestart = new Button("Restart");
        bRestart.setOnMouseClicked((event) -> {
            RandomMap rm = new RandomMap(settings);
            rm.start();
        });
        root.getChildren().add(bRestart);
        
    }
    
    public void loadValues(){
        
        File f = new File(path);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                score.add(Arrays.asList(values));
            }
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addResultToFile(Result res){
        PrintWriter writer = null;
        String output = res.toString();
        try {
            File f = new File(path);
            writer = new  PrintWriter(new FileWriter(f, true));
            writer.append(output + "\r\n");
        } catch (IOException ex) {
            Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    public Pane getRoot() {
        return root;
    }
}
