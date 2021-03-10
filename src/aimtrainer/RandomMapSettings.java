/*

 */
package aimtrainer;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * trida pro nastaveni nahodne vytvarene mapy
 * @author kubaj
 */
public class RandomMapSettings {
    private Stage stage = AimTrainer.getPrimaryStage();
    private VBox root;
    private String highscoresFileName = "random_highscores.csv";

    public RandomMapSettings() {
        //vytvoreni VBoxu
        root = new VBox();
        root.setSpacing(20.0);
        root.setPadding(new Insets(20.0));
        Rectangle2D screen = Screen.getPrimary().getBounds();
        root.setMinSize(screen.getMaxX(), screen.getMaxY());
        
        Text title = new Text("Random Map");
        title.setFont(new Font("Technic", 42));
        HBox hb = new HBox(title);
        hb.setAlignment(Pos.CENTER);
        root.getChildren().add(hb);
        
        //vytvareni vsech objektu, kterymi se upravi vlastosti nahodne mapy
        Text tApproach = new Text("Set approach time:");
        tApproach.setFont(new Font("Technic", 25));
        Slider sApproach = new Slider(1.0, 10.0, 4.0);
        sApproach.setMaxSize(1200, 70);
        sApproach.setTooltip(new Tooltip("How fast the outer circle approaches"));
        sApproach.showTickLabelsProperty().set(true);
        sApproach.showTickMarksProperty().set(true);
        sApproach.setMajorTickUnit(1.0f);
        sApproach.setBlockIncrement(0.5f);
        sApproach.setSnapToTicks(true);
        root.getChildren().add(tApproach);
        root.getChildren().add(sApproach);
        
        
        Text tTime = new Text("Set map length:");
        tTime.setFont(new Font("Technic", 25));
        Slider sTime = new Slider(10, 300, 4.0);
        sTime.setMaxSize(1200, 70);
        sTime.setTooltip(new Tooltip("Duration of the map (in seconds)"));
        sTime.showTickLabelsProperty().set(true);
        sTime.showTickMarksProperty().set(true);
        sTime.setMajorTickUnit(10.0f);
        sTime.setBlockIncrement(5.0f);
        sTime.setSnapToTicks(true);
        root.getChildren().add(tTime);
        root.getChildren().add(sTime);
        
        
        Text tSpeed = new Text("Set map speed:");
        tSpeed.setFont(new Font("Technic", 25));
        Slider sSpeed = new Slider(1, 10, 4.0);
        sSpeed.setMaxSize(1200, 70);
        sSpeed.setTooltip(new Tooltip("How quickly the targets appear after each other"));
        sSpeed.showTickLabelsProperty().set(true);
        sSpeed.showTickMarksProperty().set(true);
        sSpeed.setMajorTickUnit(1.0f);
        sSpeed.setBlockIncrement(0.5f);
        sSpeed.setSnapToTicks(true);
        root.getChildren().add(tSpeed);
        root.getChildren().add(sSpeed);
        
        
        Text tSize = new Text("Set target size:");
        tSize.setFont(new Font("Technic", 25));
        Slider sSize = new Slider(1, 10, 4.0);
        sSize.setMaxSize(1200, 70);
        sSize.setTooltip(new Tooltip("The size of the targets"));
        sSize.showTickLabelsProperty().set(true);
        sSize.showTickMarksProperty().set(true);
        sSize.setMajorTickUnit(1.0f);
        sSize.setBlockIncrement(0.5f);
        sSize.setSnapToTicks(true);
        root.getChildren().add(tSize);
        root.getChildren().add(sSize);
        
        
        Text tColors = new Text("Choose colors:");
        tColors.setFont(new Font("Technic", 25));
        root.getChildren().add(tColors);
        List<String> colors = new ArrayList<String>(){{
                        add("Red");
                        add("Green");
                        add("Blue");
                        add("Yellow");
                        add("Violet");
                        add("Dark Gray");
                        add("Gray");
                        add("Orange");
                        add("Brown");
                          }};
        //borderpane pro umisteni vyberu barvy a tlacitek vlevo a tabulky s vysledky vpravo
        BorderPane borderPane = new BorderPane();
        final List<String> selectedColorsString = new ArrayList<>();
        GridPane pane = new GridPane();
        pane.setHgap(10.0);
        pane.setVgap(10.0);
        for(int i = 0; i < colors.size(); i++){
            //vytvoreni checkboxu se vsemi barvami
            String color = colors.get(i);
            CheckBox cb = new CheckBox(color);
            cb.setFont(new Font(18));
            cb.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, 
                    Boolean oldValue, Boolean newValue) -> {
               if(newValue){
                   selectedColorsString.add(color);
               } else if (oldValue){
                   selectedColorsString.remove(color);
               }
            });
            pane.add(cb, i % 3, (int) Math.floor(i/3));
        }
        
        Button start = new Button("Launch");
        start.setFont(new Font(24));
        start.setOnMouseClicked((event) -> {
            int approachTime = (int) (1 / sApproach.getValue() * 3000);
            int time = (int) sTime.getValue();
            double speed = sSpeed.getValue();
            double size = (sSize.getValue() * 10) + 10;
            List<Color> selectedColors = getColors(selectedColorsString);
            if(selectedColors.isEmpty()){
                //kontrola jestli je vybrana nejaka barva
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("No color was selected");
                alert.setHeaderText(null);
                alert.setContentText("Choose target color");
                alert.setResult(ButtonType.OK);
                alert.showAndWait();
            } else {
                //spustit nahodne vytvorenou mapu se zadanymi vlastnostmi
                MapSettings settings = new MapSettings(approachTime, time, speed, size, selectedColors);
                RandomMap rm = new RandomMap(settings);
                //debug
                System.out.println(approachTime + "\n" + time + "\n" + speed + "\n" + size + "\n" + selectedColors);
                rm.start();
            }
        });
        VBox vb = new VBox(pane, start);
        vb.setSpacing(30.0);
        borderPane.setLeft(vb);
        
        
        //vytvoreni tabulky s vysledky
        Text tableTitle = new Text("Highscores");
        tableTitle.setFont(new Font("Technic", 26));
        Leaderboard leaderboard = new Leaderboard(highscoresFileName);
        TableView<Highscore> table = leaderboard.genLeaderboard();
        VBox tableVBox = new VBox(tableTitle,table);
        tableVBox.setSpacing(1.0);
        borderPane.setRight(tableVBox);
        root.getChildren().add(borderPane);
        
        
        Button back = new Button("Back");
        back.setFont(new Font(24));
        back.setPrefSize(110, 50);
        back.setOnMouseClicked((event) -> {
            stage.getScene().setRoot(AimTrainer.getRoot());
        });
        root.getChildren().add(back);
    }
    
    public VBox getRoot() {
        return root;
    }

    private List<Color> getColors(List<String> list) {
        //prevedeni textu na barvu
        List<Color> selected = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            switch(list.get(i).toLowerCase()){
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
            }
        }
        return selected;
    }
    
}
