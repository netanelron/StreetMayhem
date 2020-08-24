package models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
//this class is used for the menu subscenes
public class MenuSubScene extends SubScene {
	private final String FONT_URL="src/models/resources/kenvector_future.ttf";
	private final String BG_IMAGE="models/resources/BG.png";
	private boolean isHidden;
	public MenuSubScene() { //the subscene is defined as a 400x300 rectangle with given background image
		super(new AnchorPane(), 400, 300);
		isHidden=true;
		prefWidth(400);
		prefHeight(300);
		BackgroundImage bg=new BackgroundImage(new Image(BG_IMAGE), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null,
				new BackgroundSize(400,300,false,false,false,false));
		((AnchorPane)this.getRoot()).setBackground(new Background(bg));
		setLayoutX(330);
		setLayoutY(600);
		setEffect(new InnerShadow());
	}
	public void moveSubScene() { //method to show/hide the subscene
		TranslateTransition transition=new TranslateTransition();
		transition.setDuration(Duration.seconds(0.2));
		transition.setNode(this);
		if(isHidden) { //if it's hidden,show it
			transition.setToY(-450);
			isHidden=false;
		}
		else {
			transition.setToY(0); //if it's shown,hide it
			isHidden=true;
		}
		transition.play();
	}
	public boolean isShown() {
		return !isHidden;
	}
	public void setContent(String name,String text) { //set the name and content of the subscene
		Label nameLabel=new Label(name);
		Label textLabel=new Label(text);
		try {
			textLabel.setFont(Font.loadFont(new FileInputStream(FONT_URL), 20));
			nameLabel.setFont(Font.loadFont(new FileInputStream(FONT_URL), 25));
		}
		catch(FileNotFoundException e) {
			textLabel.setFont(Font.font("Impact", 25));
			nameLabel.setFont(Font.font("Impact", 25));
		}
		textLabel.setTextFill(Color.FLORALWHITE);
		nameLabel.setTextFill(Color.DIMGREY);
		nameLabel.setEffect(new Bloom());
		nameLabel.setLayoutX(20);
		nameLabel.setLayoutY(15);
		textLabel.setLayoutX(20);
		textLabel.setLayoutY(55);
		((AnchorPane)this.getRoot()).getChildren().addAll(textLabel,nameLabel);
	}
}
