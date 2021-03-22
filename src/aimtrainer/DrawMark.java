package aimtrainer;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DrawMark {
    protected ImageView imgView;
    protected SequentialTransition seqT;

    public DrawMark (Circle c){
        //nastaveni velikost a pozice obrazku
        imgView = new ImageView();
        imgView.setFitWidth(1.2*c.getRadius());
        imgView.setFitHeight(1.2*c.getRadius());

        //opatreni proti nastaveni pouze LayoutX v SceneBuilderu
        double imgX = c.getCenterX();
        double imgY = c.getCenterY();
        if(c.getCenterX() == 0.0) imgX = c.getLayoutX();
        if(c.getCenterY() == 0.0) imgY = c.getLayoutY();

        imgView.setX(imgX-(imgView.getFitWidth()/2));
        imgView.setY(imgY-(imgView.getFitHeight()/2));

    }

    public void draw(boolean hit){
        //vybere obrazek bud fajfky nebo krizku
        if(hit){
            imgView.setImage(new Image("check.png"));
        } else {
            imgView.setImage(new Image("cross.png"));
        }

        //definuje animace pro objeveni a zmizeni obrazku a vsechny animace sjednoti
        seqT = new SequentialTransition();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), imgView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        FadeTransition stay = new FadeTransition(Duration.millis(200), imgView);
        stay.setFromValue(1.0);
        stay.setToValue(1.0);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), imgView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        seqT.getChildren().addAll(fadeIn,stay,fadeOut);
        seqT.play();
    }

    public ImageView getImgView() {
        return imgView;
    }

    public SequentialTransition getSeqT() {
        return seqT;
    }
}
