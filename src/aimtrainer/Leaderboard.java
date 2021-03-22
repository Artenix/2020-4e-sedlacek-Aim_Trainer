/*

 */
package aimtrainer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * trida vytvarejici tabulku s vysledky
 * @author kubaj
 */
public class Leaderboard {
    private String path;
    private ObservableList<Result> results = FXCollections.observableArrayList();
    
    public Leaderboard(String highscoresFileName) {
        //z nazvu vytvori absolutni cestu k souboru s vysledky ve slozce highscores
        this.path = (new File("").getAbsolutePath()).concat("\\highscores\\" + highscoresFileName);
        System.out.println(path);
        loadValues();
        Collections.sort(results);
        Collections.reverse(results);
    }
    
    public void loadValues(){
        //nacte hodnoty ze souboru s vysledky (pokud soubor existuje)
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
        //vytvori tabulku s vysledky
        TableView<Highscore> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");
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
        
        

        table.getColumns().addAll(tcPos, tcScore);
        /*table.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldN, Number newN) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) -> {
                header.setReordering(false);
            });
        });*/
        
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
        
        //upravi nastaveni popisku jednotlivych rad
        table.setRowFactory(new Callback<TableView<Highscore>, TableRow<Highscore>>() {
            @Override
            public TableRow call(final TableView p) {
                TableRow row = new TableRow<Highscore>() {
                    @Override
                    public void updateItem(Highscore item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setTooltip(null);
                        } else {
                            //pro kazdou radu vytvori novy popisek kteremu upravi chovani
                            Tooltip tooltip = new Tooltip();
                            tooltip.setShowDelay(Duration.millis(200));
                            tooltip.setText(item.getDetails());
                            setTooltip(tooltip);
                        }
                    }
                    
                };
                return row;
            }
        });
        
        return table;
        
    }
}
