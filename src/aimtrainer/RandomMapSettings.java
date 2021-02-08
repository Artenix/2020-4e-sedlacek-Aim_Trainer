/*

 */
package aimtrainer;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author kubaj
 */
public class RandomMapSettings {
    private Stage stage = AimTrainer.getPrimaryStage();
    private VBox root;
    private String path = "C:\\Users\\kubaj\\Desktop\\random_highscores.csv";
    private ObservableList<Result> results;

    public RandomMapSettings() {
        
        root = new VBox();
        root.setSpacing(20.0);
        root.setPadding(new Insets(20.0));
        Rectangle2D screen = Screen.getPrimary().getBounds();
        root.setMinSize(screen.getMaxX(), screen.getMaxY());
        
        
        Text tApproach = new Text("Nastavit rychlost přibližování:");
        tApproach.setFont(new Font("Technic", 25));
        Slider sApproach = new Slider(1.0, 10.0, 4.0);
        sApproach.setMaxSize(1200, 70);
        sApproach.setTooltip(new Tooltip("Jakou rychlostí se bude přibližovat vnější kruh"));
        sApproach.showTickLabelsProperty().set(true);
        sApproach.showTickMarksProperty().set(true);
        sApproach.setMajorTickUnit(1.0f);
        sApproach.setBlockIncrement(0.5f);
        sApproach.setSnapToTicks(true);
        root.getChildren().add(tApproach);
        root.getChildren().add(sApproach);
        
        
        Text tTime = new Text("Nastavit délku mapy:");
        tTime.setFont(new Font("Technic", 25));
        Slider sTime = new Slider(10, 300, 4.0);
        sTime.setMaxSize(1200, 70);
        sTime.showTickLabelsProperty().set(true);
        sTime.showTickMarksProperty().set(true);
        sTime.setMajorTickUnit(10.0f);
        sTime.setBlockIncrement(5.0f);
        sTime.setSnapToTicks(true);
        root.getChildren().add(tTime);
        root.getChildren().add(sTime);
        
        
        Text tSpeed = new Text("Nastavit rychlost mapy:");
        tSpeed.setFont(new Font("Technic", 25));
        Slider sSpeed = new Slider(1, 10, 4.0);
        sSpeed.setMaxSize(1200, 70);
        sSpeed.showTickLabelsProperty().set(true);
        sSpeed.showTickMarksProperty().set(true);
        sSpeed.setMajorTickUnit(1.0f);
        sSpeed.setBlockIncrement(0.5f);
        sSpeed.setSnapToTicks(true);
        root.getChildren().add(tSpeed);
        root.getChildren().add(sSpeed);
        
        
        Text tSize = new Text("Nastavit velikost terčů:");
        tSize.setFont(new Font("Technic", 25));
        Slider sSize = new Slider(1, 10, 4.0);
        sSize.setMaxSize(1200, 70);
        sSize.showTickLabelsProperty().set(true);
        sSize.showTickMarksProperty().set(true);
        sSize.setMajorTickUnit(1.0f);
        sSize.setBlockIncrement(0.5f);
        sSize.setSnapToTicks(true);
        root.getChildren().add(tSize);
        root.getChildren().add(sSize);
        
        
        Text tColors = new Text("Vyberte barvy:");
        tColors.setFont(new Font("Technic", 25));
        root.getChildren().add(tColors);
        List<String> colors = new ArrayList<String>(){{
                        add("Červená");
                        add("Zelená");
                        add("Modrá");
                        add("Žlutá");
                        add("Fialová");
                        add("Černá");
                        add("Šedá");
                        add("Oranžová");
                        add("Hnědá");
                          }};
        
        BorderPane borderPane = new BorderPane();
        final List<String> selectedColorsString = new ArrayList<>();
        GridPane pane = new GridPane();
        pane.setHgap(10.0);
        pane.setVgap(10.0);
        for(int i = 0; i < colors.size(); i++){
            
            String color = colors.get(i);
            CheckBox cb = new CheckBox(color);
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
        
        Button start = new Button("Spustit");
        start.setOnMouseClicked((event) -> {
            int approachTime = (int) (1 / sApproach.getValue() * 3000);
            int time = (int) sTime.getValue();
            double speed = sSpeed.getValue();
            double size = (sSize.getValue() * 10) + 10;
            List<Color> selectedColors = getColors(selectedColorsString);
            if(selectedColors.isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Není vybraná žádná barva");
                alert.setHeaderText(null);
                alert.setContentText("Vyber barvu kruhů");
                alert.setResult(ButtonType.OK);
                alert.showAndWait();
            } else {
                MapSettings settings = new MapSettings(approachTime, time, speed, size, selectedColors);
                RandomMap rm = new RandomMap(settings);
                System.out.println(approachTime + "\n" + time + "\n" + speed + "\n" + size + "\n" + selectedColors);
                rm.start();
            }
        });
        VBox vb = new VBox(pane, start);
        vb.setSpacing(30.0);
        borderPane.setLeft(vb);
        
        results = FXCollections.observableArrayList();
        loadValues();
        Collections.sort(results);
        Collections.reverse(results);
        TableView<Highscore> table = genLeaderboard();
        
        borderPane.setRight(table);
        root.getChildren().add(borderPane);
        
        
        Button back = new Button("Zpět");
        back.setPrefSize(110, 50);
        back.setOnMouseClicked((event) -> {
            stage.getScene().setRoot(AimTrainer.getRoot());
        });
        root.getChildren().add(back);
        
    }
    
    public VBox getRoot() {
        return root;
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
                String[] strValues = line.split(",");
                Result res = new Result(Integer.parseInt(strValues[0]), Integer.parseInt(strValues[1]),
                                    Integer.parseInt(strValues[2]), Integer.parseInt(strValues[3]), Long.parseLong(strValues[4]));
                results.add(res);
            }
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TableView<Highscore> genLeaderboard(){
        
        TableView<Highscore> table = new TableView<>();
        
        table.setMinWidth(519);
        table.setEditable(false);
        
        TableColumn tcPos = new TableColumn("Position");
        tcPos.setResizable(false);
        tcPos.setSortable(false);
        tcPos.setCellValueFactory(new PropertyValueFactory<Highscore, Integer>("pos"));
        tcPos.setMinWidth(100);
        
        TableColumn tcScore = new TableColumn("Score");
        tcScore.setResizable(false);
        tcScore.setSortable(false);
        tcScore.setCellValueFactory(new PropertyValueFactory<Highscore, String>("score"));
        tcScore.setMinWidth(400);
        
        
        //zakazani presouvani sloupcu
        table.getColumns().addAll(tcPos, tcScore);
        table.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldN, Number newN) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) -> {
                header.setReordering(false);
            });
        });
        
        //zaplnit tabulku vysledku
        int i = 0;
        ObservableList<Highscore> data = FXCollections.observableArrayList();
        DateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
        for(Result res : results){
            i++;
            data.add(new Highscore(i, res.getScore() + "\n" + res.getHighestCombo() + "x",
                            "Hit: " + res.getHit() + " Missed: " + res.getMissed() + " - " + format.format(new Date(res.getTimeAchieved()))));
        }
        table.setItems(data);
        
        table.setRowFactory(new Callback<TableView<Highscore>, TableRow<Highscore>>() {
            @Override
            public TableRow call(final TableView p) {
                return new TableRow<Highscore>() {
                    @Override
                    public void updateItem(Highscore item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setTooltip(null);
                        } else {
                            Tooltip tooltip = new Tooltip();
                            changeTooltipBehaviour(tooltip);
                            tooltip.setText(item.getDetails());
                            setTooltip(tooltip);
                        }
                    }
                };
            }
        });
        
        return table;
        
    }
    
    private void changeTooltipBehaviour(Tooltip tooltip){
        try {
            Class<?> tooltipClass = tooltip.getClass().getDeclaredClasses()[0];
            Constructor<?> constructor = tooltipClass.getDeclaredConstructor(Duration.class, 
                    Duration.class, Duration.class, boolean.class);
            constructor.setAccessible(true);
            Object tooltipDurations = constructor.newInstance(new Duration(200), new Duration(10000), new Duration(200), true);
            Field field = tooltip.getClass().getDeclaredField("BEHAVIOR");
            field.setAccessible(true);
            field.set(tooltip, tooltipDurations);
        } catch (Exception e) {
            Logger.getLogger(RandomMapSettings.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private List<Color> getColors(List<String> list) {
        List<Color> selected = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            switch(list.get(i)){
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
