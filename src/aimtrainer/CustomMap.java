/*

 */
package aimtrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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
 *
 * @author kubaj
 */
public class CustomMap extends Thread {
    
    private Stage stage = AimTrainer.getPrimaryStage();
    private Pane root;
    private final ArrayList<Node> children;
    private int approachTime;
    private int time;
    private double speed;
    private double size;
    private List<Color> colors = new ArrayList<Color>();
    private MapSettings settings;
    private Rectangle2D screen = Screen.getPrimary().getBounds();
    private double minX = screen.getMinX() + 80;
    private double minY = screen.getMinY() + 80;
    private double maxX = screen.getMaxX() - 80;
    private double maxY = screen.getMaxY() - 80;
    private int hit;
    private int missed;
    private double accuracy;
    private final IntegerProperty score = new SimpleIntegerProperty();
    private double multiplier;
    private int highestCombo;
    private final IntegerProperty combo = new SimpleIntegerProperty();

    public CustomMap(MapSettings settings, ObservableList<Node> children) {
        this.settings = settings;
        this.approachTime = settings.getApproachTime();
        this.time = settings.getTime();
        this.speed = settings.getSpeed();
        this.size = settings.getSize();
        this.colors = settings.getColors();
        this.children = new ArrayList<Node>(children);
        multiplier = (speed/5) + (50/size);
        root = new Pane();
        
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
        Circle c1 = new Circle(minX, minY, 5);
        Circle c2 = new Circle(maxX, minY, 5);
        Circle c3 = new Circle(minX, maxY, 5);
        Circle c4 = new Circle(maxX, maxY, 5);
        root.getChildren().addAll(c1, c2, c3, c4);
        
        root.getChildren().addAll(scoreLabel, comboLabel);
        stage.getScene().setRoot(root);
    }
    
    private void generateCircle(Circle target){
        Random rand = new Random();
        Color color = colors.get(rand.nextInt(colors.size()));
        target.fillProperty().set(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));
        target.setRadius(size);
        target.setStroke(color);
        target.setStrokeType(StrokeType.CENTERED);
        target.setStrokeWidth(size/15.0);
        
        ApproachCircleGen acg = new ApproachCircleGen(target.getCenterX(), target.getCenterY(), size, approachTime, color);
        Platform.runLater(() ->  root.getChildren().add(acg.getCircle()));
        PauseTransition pt = new PauseTransition(Duration.millis(approachTime));
        pt.setOnFinished(event -> {
           if(root.getChildren().contains(target)){
               acg.stopAnimation();
               Platform.runLater(() -> root.getChildren().remove(target));
               if(combo.get() > highestCombo){
                   highestCombo = combo.get();
               }
               combo.set(0);
               missed++;
           }
        });

        pt.play();
        acg.startAnimation();
        long startTime = System.currentTimeMillis();
        target.fillProperty().set(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.65));
        
        target.setOnMouseClicked((MouseEvent event) -> {
            acg.stopAnimation();
            long stopTime = System.currentTimeMillis();
            long reaction = stopTime - startTime;
            if(reaction < approachTime){
                combo.set(combo.get() + 1);
                int i = score.get() + (int) ((approachTime - reaction) * multiplier * combo.get());
                score.set(i);
                hit++;
                System.out.println("\nReaction: " + reaction + "\nMultiplier: " + multiplier + "\ncombo: " + combo.get());
                System.out.println("Score: " + score.get() + " - " + i);
            }
            Platform.runLater(() -> root.getChildren().remove(target));
        });
        
        Platform.runLater(() -> root.getChildren().add(target));
    }
    
    @Override
    public void run() {
        try {
            missed = 0;
            score.set(0);
            combo.set(0);
            Text t = new Text();
            t.setFont(new Font("Technic", 110));
            t.setX(screen.getMaxX()/2);
            t.setY(screen.getMaxY()/2 - 80);
            Text t2 = new Text("Klikni na terče co nejrychleji");
            t2.setFont(new Font("Technic", 30));
            t2.setX(t.getX());
            t2.setY(t.getY() + 60);
            centerText(t2);
            FadeTransition ft2 = new FadeTransition(Duration.millis(3000), t2);
            ft2.setFromValue(1.5);
            ft2.setToValue(0.0);
            ft2.play();
            Platform.runLater(() -> root.getChildren().add(t));
            Platform.runLater(() -> root.getChildren().add(t2));
            for(int i = 3; i > 0; i--){
                t.setText(String.valueOf(i));
                centerText(t);
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
        
        int interval = (int) (1000 - Math.pow(speed, 2.9));
        for(int i = 0; i < children.size(); i++){
            Node node = children.get(i);
            try {
                generateCircle((Circle) node);
                Thread.sleep(interval);
            } catch (ClassCastException ex){
                    Logger.getLogger(AimTrainer.class.getName()).log(Level.SEVERE, null, ex); //otestovat
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if(combo.get() > highestCombo){
                highestCombo = combo.get();
            }
            Result res = new Result(hit, missed, score.get(), highestCombo, System.currentTimeMillis());
            MapSettings settings = new MapSettings(approachTime, interval*children.size(), speed, size, colors);
            ResultScreen resScreen = new ResultScreen(res, settings);
            stage.getScene().setRoot(resScreen.getRoot());
            
            
        /*Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(interval), event -> {
                try {
                    final int i = currCycle;
                    Circle c = (Circle) children.get(i);
                    c.setVisible(true);
                    generateCircle(c);
                    currCycle += 1;
                } catch (ClassCastException ex){
                    Logger.getLogger(AimTrainer.class.getName()).log(Level.SEVERE, null, ex); //otestovat
                }
            })
        );
        int cycleCount = (time * 1000) / interval;
        timeline.setCycleCount(cycleCount);
        timeline.play();
        timeline.setOnFinished(event -> {
            if(combo.get() > highestCombo){
                highestCombo = combo.get();
            }
            Result res = new Result(hit, missed, score.get(), highestCombo, System.currentTimeMillis());
            MapSettings settings = new MapSettings(approachTime, time, speed, size, colors);
            ResultScreen resScreen = new ResultScreen(res, settings);
            stage.getScene().setRoot(resScreen.getRoot());
        });*/
    }
    
    public void centerText(Text t){
        double x = t.getX();
        double y = t.getY();
        double width = t.getBoundsInLocal().getWidth();
        double height = t.getBoundsInLocal().getHeight();
        t.relocate(x - (width / 2), y - (height / 2));
    }
}
