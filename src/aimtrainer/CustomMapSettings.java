/*

 */
package aimtrainer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * trida se seznamem vsech vlastnich map
 * slouzi jako menu pro vyber mapy a zobrazeni nejlepsich skore
 * @author kubaj
 */
class CustomMapSettings {
    private Stage stage = AimTrainer.getPrimaryStage();
    private BorderPane root;
    private BorderPane bpLeft;
    private ObservableList<CustomMapProp> maps = FXCollections.observableArrayList();

    public CustomMapSettings() {
        root = new BorderPane();
        root.setPadding(new Insets(20.0));
        Rectangle2D screen = Screen.getPrimary().getBounds();
        root.setMinSize(screen.getMaxX(), screen.getMaxY());
        
        Text title = new Text("Custom Maps");
        title.setFont(new Font("Technic", 42));
        HBox hb = new HBox(title);
        hb.setAlignment(Pos.CENTER);
        root.setTop(hb);
        
        //nastaveni prave casti panu
        loadCustomMaps();
        TableView<CustomMapProp> table = genMapList();
        root.setRight(table);
        
        //nastaveni leve casti panu
        bpLeft = new BorderPane();
        Button back = new Button("Back");
        back.setFont(new Font(24));
        back.setPrefSize(110, 50);
        back.setOnMouseClicked((event) -> {
            stage.getScene().setRoot(AimTrainer.getRoot());
        });
        bpLeft.setBottom(back);
        root.setLeft(bpLeft);
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    private void loadCustomMaps() {
        //nacteni vsech vlastnich map ve slozce maps
        File dir = new File("maps");
        if(!dir.exists()){
            try {
                Files.createDirectory(Paths.get("maps"));
            } catch (IOException ex) {
                Logger.getLogger(ResultScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        File[] files = dir.listFiles();
        //projde vsechny soubory ve slozce a vybere pouze ty s koncovkou fxml
        for(File f : files){
            if(f.isFile() && f.getName().endsWith("fxml")){
                String name = f.getName().substring(0, f.getName().length()-5);
                FXMLLoader loader;
                try {
                    //do seznamu vlastnich map prida zpracovanou mapu z konkretniho souboru fxml
                    loader = new FXMLLoader(f.toURI().toURL());
                    Parent parent = loader.load();
                    CustomMap cm = ((CustomMapController) loader.getController()).getCustomMap();
                    DateFormat format = new SimpleDateFormat("mm:ss");
                    String formattedTime = format.format(new Date(cm.getTime()));
                    String details = "AR: " + cm.getApproachTime() + "ms Time: " + formattedTime + " Speed: " + cm.getSpeed() 
                            + " Size: " + cm.getSize();
                    CustomMapProp cmp = new CustomMapProp(name, details, cm);
                    maps.add(cmp);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CustomMapSettings.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CustomMapSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    }
    
    public TableView<CustomMapProp> genMapList(){
        //vytvoreni tabulky s vlastnimi mapami
        TableView<CustomMapProp> table = new TableView<>();
        
        table.setMinWidth(519);
        table.setEditable(false);
        
        TableColumn tColumn = new TableColumn("Maps");
        tColumn.setResizable(false);
        tColumn.setSortable(false);
        tColumn.setCellValueFactory(new PropertyValueFactory<Highscore, String>("description"));
        tColumn.setMinWidth(500);
        
        
        table.getColumns().add(tColumn);
        
        //zaplni tabulku daty
        ObservableList<CustomMapProp> data = FXCollections.observableArrayList();
        if(maps != null){
            for(CustomMapProp map : maps){
                data.add(map);
            }
        }
        table.setItems(data);
        
        //klikani na radek tabulky
        table.setRowFactory(new Callback<TableView<CustomMapProp>, TableRow<CustomMapProp>>() {
            @Override
            public TableRow call(final TableView p) {
                TableRow row = new TableRow<CustomMapProp>();
                row.setOnMouseClicked(event -> {
                    if(!row.isEmpty()){
                        String highscoresFileName = table.getSelectionModel().getSelectedItem().getName() + ".csv";
                        if(event.getClickCount() == 1){
                            //jedno kliknuti pro zobrazeni tabulky s vysledkami
                            Leaderboard leaderboard = new Leaderboard(highscoresFileName);
                            TableView<Highscore> table = leaderboard.genLeaderboard();
                            Node n = root.lookup("table");
                            if(n == null){
                                root.getChildren().remove(n);
                            }
                            bpLeft.setTop(table);
                        } else if(event.getClickCount() == 2){
                            //dvojklik pro zapnuti mapy
                            table.getSelectionModel().getSelectedItem().startMap();
                        }
                       
                    }
                });
                return row;
            }
        });
        
        return table;
        
    }
}
