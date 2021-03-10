/*

 */
package aimtrainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * trida zobrazujici dosazeny vysledek na mape
 * @author kubaj
 */
public class ResultScreen {
    private Stage stage = AimTrainer.getPrimaryStage();
    private BorderPane root;
    private Result res;
    private String path;
    private String mapType;
    private MapSettings settings;

    public ResultScreen(Result res, MapSettings settings, String highscoresFileName, String mapType) {
        this.settings = settings;
        this.res = res;
        this.path = (new File("").getAbsolutePath()).concat("\\highscores\\" + highscoresFileName);
        this.mapType = mapType;
        
        if(res.getScore() > 0) {
            addResultToFile(res);
        }
        
        //vytvoreni vypisu skore atd. na mape
        root = new BorderPane();
        root.setPadding(new Insets(20,300,0,100));
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        
        
        Text title = new Text();
        title.setFont(new Font("Futura", 72));
        if(mapType.equals("random")){
            title.setText("Random Map");
        } else if (mapType.equals("custom")){
            title.setText(highscoresFileName.substring(0, highscoresFileName.length()-4));
        }
        HBox hb = new HBox(title);
        hb.setAlignment(Pos.TOP_CENTER);
        root.setTop(hb);
        
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
        Text tCombo = new Text(String.valueOf(res.getHighestCombo()) + "x");
        tCombo.setFont(font);
        grid.add(lCombo, 0, 3);
        grid.add(tCombo, 1, 3);
        
        
        Button bRestart = new Button("Restart");
        bRestart.setFont(new Font(32));
        bRestart.setPadding(Insets.EMPTY);
        bRestart.setPrefSize(130, 70);
        bRestart.setOnMouseClicked((event) -> {
            RandomMap rm = new RandomMap(settings);
            rm.start();
        });
        Button bBack = new Button("Back");
        bBack.setFont(new Font(32));
        bBack.setPrefSize(130, 70);
        bBack.setOnMouseClicked((event) -> {
            if(mapType.equals("random")){
                RandomMapSettings rms = new RandomMapSettings();
                stage.getScene().setRoot(rms.getRoot());
            } else if (mapType.equals("custom")){
                CustomMapSettings cms = new CustomMapSettings();
                stage.getScene().setRoot(cms.getRoot());
            }
            
        });
        //prazdny label pro pridani mezery mezi 1. a 3. sloupcem
        Label empty = new Label();
        empty.setMinWidth(200);
        grid.add(empty, 2, 0);
        grid.add(bRestart, 3, 2);
        grid.add(bBack, 3, 3);
        root.setCenter(grid);
        
    }
    
    public void addResultToFile(Result res){
        //prida dosazeny vysledek na konec souboru
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
