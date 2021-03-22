/*

 */
package aimtrainer;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * hlavni trida, ktera se spusti jako prvni
 * obsahuje hlavni menu
 * @author kubaj
 */
public class AimTrainer extends Application {
    private static Stage stage;
    private static Pane root;
    private int approachTime = 1000; //ms
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        root = new Pane();
        Rectangle2D screen = Screen.getPrimary().getBounds();
        Scene scene = new Scene(root, screen.getMaxX(), screen.getMaxY());
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11){
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        
        
        //nastaveni StackPanu s prvnim kruhem pro vyber mapy s nahodnymu terci
        StackPane spRandom = new StackPane();
        Circle cRandom = new Circle(100);
        Color blue = Color.DODGERBLUE;
        cRandom.fillProperty().set(new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), 0.5));
        cRandom.setStroke(blue);
        cRandom.setStrokeType(StrokeType.CENTERED);
        cRandom.setStrokeWidth(6.0);
        Text tRandom = new Text("Random Map");
        tRandom.setFont(new Font("Technic", 26));
        ApproachCircleGen acgRandom = new ApproachCircleGen(cRandom.getCenterX(), cRandom.getCenterY(), 100.0, approachTime, blue);
        
        //pridani textu a kruhu do StackPanu a nastaveni akci pri prejeti mysi a kliknuti mysi
        spRandom.getChildren().add(acgRandom.getCircle());
        spRandom.setOnMouseEntered((MouseEvent event) -> {
            acgRandom.startAnimation();
            cRandom.fillProperty().set(new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), 0.65));
        });
        spRandom.setOnMouseExited((MouseEvent event) -> {
            acgRandom.stopAnimation();
        });
        spRandom.setOnMouseClicked((MouseEvent event) -> {
            RandomMapSettings rms = new RandomMapSettings();
            primaryStage.getScene().setRoot(rms.getRoot());
        });
        
        //pridani objektu do StackPanu a nastaveni pozice
        spRandom.getChildren().addAll(cRandom, tRandom);
        spRandom.setLayoutX((root.getWidth()/2) - (cRandom.getRadius()) - 250);
        spRandom.setLayoutY((root.getHeight()/3) - (cRandom.getRadius()));
        root.getChildren().add(spRandom);
        
        
        //nastaveni StackPanu s druhym kruhem pro vyber nebo vytvoreni vlastni mapy
        StackPane spCustom = new StackPane();
        Circle cCustom = new Circle(100);
        Color green = Color.LIMEGREEN;
        cCustom.fillProperty().set(new Color(green.getRed(), green.getGreen(), green.getBlue(), 0.5));
        cCustom.setStroke(green);
        cCustom.setStrokeType(StrokeType.CENTERED);
        cCustom.setStrokeWidth(6.0);
        Text tCustom = new Text("Custom Map");
        tCustom.setFont(new Font("Technic", 26));
        ApproachCircleGen acgCustom = new ApproachCircleGen(cCustom.getCenterX(), cCustom.getCenterY(), 100.0, approachTime, green);
        
        //pridani textu a kruhu do StackPanu a nastaveni akci pri prejeti mysi a kliknuti mysi
        spCustom.getChildren().add(acgCustom.getCircle());
        spCustom.setOnMouseEntered((MouseEvent event) -> {
            acgCustom.startAnimation();
            cCustom.fillProperty().set(new Color(green.getRed(), green.getGreen(), green.getBlue(), 0.65));
        });
        spCustom.setOnMouseExited((MouseEvent event) -> {
            acgCustom.stopAnimation();
        });
        spCustom.setOnMouseClicked((MouseEvent event) -> {
                CustomMapSettings cms = new CustomMapSettings();
                primaryStage.getScene().setRoot(cms.getRoot());
        });
        //pridani objektu do StackPanu a nastaveni pozice
        spCustom.getChildren().addAll(cCustom, tCustom);
        spCustom.setLayoutX((root.getWidth()/2) - (cCustom.getRadius()) + 250);
        spCustom.setLayoutY((root.getHeight()/3) - (cCustom.getRadius()));
        root.getChildren().add(spCustom);
        
        
        //nastaveni StackPanu s tretim kruhem pro ukonceni aplikace
        StackPane spExit = new StackPane();
        Circle cExit = new Circle(100);
        Color red = Color.CRIMSON;
        cExit.fillProperty().set(new Color(red.getRed(), red.getGreen(), red.getBlue(), 0.5));
        cExit.setStroke(red);
        cExit.setStrokeType(StrokeType.CENTERED);
        cExit.setStrokeWidth(6.0);
        Text tExit = new Text("Exit");
        tExit.setFont(new Font("Technic", 26));
        ApproachCircleGen acgExit = new ApproachCircleGen(cExit.getCenterX(), cExit.getCenterY(), 100.0, approachTime, red);
        //pridani textu a kruhu do StackPanu a nastaveni akci pri prejeti mysi a kliknuti mysi
        spExit.getChildren().add(acgExit.getCircle());
        spExit.setOnMouseEntered((MouseEvent event) -> {
            acgExit.startAnimation();
            cExit.fillProperty().set(new Color(red.getRed(), red.getGreen(), red.getBlue(), 0.65));
        });
        spExit.setOnMouseExited((MouseEvent event) -> {
            acgExit.stopAnimation();
        });
        spExit.setOnMouseClicked((MouseEvent event) -> {
            primaryStage.close();
        });
        //pridani objektu do StackPanu a nastaveni pozice
        spExit.getChildren().addAll(cExit, tExit);
        spExit.setLayoutX((root.getWidth()/2) - (cExit.getRadius()));
        spExit.setLayoutY((root.getHeight()/3) - (cExit.getRadius()) + 300);
        root.getChildren().add(spExit);
        
        //zakladni nastaveni okna
        primaryStage.setTitle("Aim Trainer");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getPrimaryStage() {
        return stage;
    }
    
    public static Pane getRoot() {
        return root;
    }
    
}
