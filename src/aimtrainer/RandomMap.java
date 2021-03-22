/*

 */
package aimtrainer;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
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
 * trida obsahujici nahodne vytvorenou mapu
 * @author kubaj
 */
public class RandomMap extends Thread {
    private Stage stage = AimTrainer.getPrimaryStage();
    private Pane root;
    private int approachTime;
    private int time;
    private double speed;
    private double size;
    private List<Color> colors;
    private MapSettings settings;
    private Rectangle2D screen = Screen.getPrimary().getBounds();
    private double minX = screen.getMinX() + 100;
    private double minY = screen.getMinY() + 100;
    private double maxX = screen.getMaxX() - 100;
    private double maxY = screen.getMaxY() - 100;
    private int hit;
    private int missed;
    private double accuracy;
    private final IntegerProperty score = new SimpleIntegerProperty();
    private double multiplier;
    private int highestCombo;
    private final IntegerProperty combo = new SimpleIntegerProperty();
    

    public RandomMap(MapSettings settings) {
        this.settings = settings;
        this.approachTime = settings.getApproachTime();
        this.time = settings.getTime();
        this.speed = settings.getSpeed();
        this.size = settings.getSize();
        this.colors = settings.getColors();
        multiplier = speed * (500/size) * (3000/approachTime);
        root = new Pane();

        
        //pridani HUD
        Label scoreLabel = new Label();
        scoreLabel.setPadding(new Insets(20));
        scoreLabel.setFont(new Font("Technic", 30));
        scoreLabel.textProperty().bind(score.asString());
        
        Label comboLabel = new Label();
        comboLabel.setLayoutY(maxY + 15);
        comboLabel.setPadding(new Insets(20));
        comboLabel.setFont(new Font("Technic", 30));
        comboLabel.textProperty().bind(combo.asString().concat("x"));
        
        //debug
        /*Circle c1 = new Circle(minX, minY, 5);
        Circle c2 = new Circle(maxX, minY, 5);
        Circle c3 = new Circle(minX, maxY, 5);
        Circle c4 = new Circle(maxX, maxY, 5);
        root.getChildren().addAll(c1, c2, c3, c4);*/
        
        root.getChildren().addAll(scoreLabel, comboLabel);
        stage.getScene().setRoot(root);
    }
    
    public void generateRandom(){
        //vytvori terc nahodne na obrazovce s nahodnou barvou
        Random rand = new Random();
        double x = (rand.nextDouble() * (maxX-minX)) + minX;
        double y = (rand.nextDouble() * (maxY-minY)) + minY;
        Color color = colors.get(rand.nextInt(colors.size()));
        Circle c = new Circle(x, y, size);
        c.fillProperty().set(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));
        c.setStroke(color);
        c.setStrokeType(StrokeType.CENTERED);
        c.setStrokeWidth(size/15.0);
        
        //nastaveni vykresleni checkmarku nebo krizku
        DrawMark dm = new DrawMark(c);
        Platform.runLater(() -> root.getChildren().add(dm.getImgView()));
        //nastaveni priblizovaciho kruhu
        ApproachCircleGen acg = new ApproachCircleGen(x, y, size, approachTime, color);
        Platform.runLater(() -> root.getChildren().add(acg.getCircle()));
        //zmizeni terce po chvili
        PauseTransition pt = new PauseTransition(Duration.millis(approachTime));
        pt.setOnFinished(event -> {
               acg.stopAnimation();
               Platform.runLater(() -> root.getChildren().remove(c));
               dm.draw(false);
               if(combo.get() > highestCombo){
                   highestCombo = combo.get();
               }
               combo.set(0);
               missed++;
        });

        pt.play();
        acg.startAnimation();
        Platform.runLater(() -> root.getChildren().add(c));
        long startTime = System.currentTimeMillis();
        c.fillProperty().set(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.65));
        
        //pri kliknuti na terc
        c.setOnMouseClicked((MouseEvent event) -> {
            pt.stop();
            long stopTime = System.currentTimeMillis();
            Platform.runLater(() -> root.getChildren().remove(c));
            acg.stopAnimation();
            long reaction = stopTime - startTime;
            dm.draw(true);
            combo.set(combo.get() + 1);
            double base = (1.0/reaction) * 1000;
            int i = score.get() + (int) (base * multiplier * combo.get());
            score.set(i);
            hit++;
            //debug
            System.out.println("\nReaction: " + reaction + "\nMultiplier: " + multiplier + "\ncombo: " + combo.get());
            System.out.println("Score: " + score.get() + " - " + i);
        });
    }
    
    public void centerText(Text t){
        //vycentrovat text do stredu obrazovky
        double x = t.getX();
        double y = t.getY();
        double width = t.getLayoutBounds().getWidth();
        double height = t.getLayoutBounds().getHeight();
        t.relocate(x - (width / 2), y - (height / 2));
    }

    @Override
    public void run() {
        //spustit vlakno, ve kterem se generuji terce
        try {
            missed = 0;
            score.set(0);
            combo.set(0);
            //pocatecni odpocet
            Text t = new Text();
            t.setFont(new Font("Technic", 110));
            t.setX(screen.getMaxX()/2);
            t.setY(screen.getMaxY()/2 - 80);
            Text t2 = new Text("Klikni na terce co nejrychleji");
            t2.setFont(new Font("Technic", 30));
            t2.setX(t.getX());
            t2.setY(t.getY() + 60);
            centerText(t2);
            //zmizeni textu
            FadeTransition ft2 = new FadeTransition(Duration.millis(3000), t2);
            ft2.setFromValue(1.5);
            ft2.setToValue(0.0);
            ft2.play();
            Platform.runLater(() -> root.getChildren().add(t));
            Platform.runLater(() -> root.getChildren().add(t2));
            for(int i = 3; i > 0; i--){
                t.setText(String.valueOf(i));
                centerText(t);
                //mizeni cisel
                FadeTransition ft = new FadeTransition(Duration.millis(900), t);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                Thread.sleep(1000);
            }
            Platform.runLater(() -> root.getChildren().remove(t));
            
        } catch (InterruptedException ex) {
            System.out.println("Thread has been interrupted");
        }
        //vytvoreni casove osy ktera pri kazdem cyklu vytvori novy terc
        int interval = (int) (1000 - Math.pow(speed, 2.9));
        int cycleCount = (time * 1000) / interval;
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(interval), event -> {
                generateRandom();
            })
        );
        timeline.setCycleCount(cycleCount);
        timeline.play();
        timeline.setOnFinished(event -> {
            //pri ukonceni zaznamena vysledek, nastaveni mapy a prepne na vyslednou obrazovku
            if (combo.get() > highestCombo) {
                highestCombo = combo.get();
            }
            Result res = new Result(hit, missed, score.get(), highestCombo, System.currentTimeMillis());
            MapSettings settings = new MapSettings(approachTime, time, speed, size, colors);
            ResultScreen resScreen = new ResultScreen(res, settings, "random_highscores.csv", "random");
            stage.getScene().setRoot(resScreen.getRoot());
        });
    }
}
