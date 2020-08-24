package models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
//this class is for the buttons in the game
public class GameButton extends Button {
	private String name;
	private static final int WIDTH=300;
	private static final int HEIGHT=44;
	private static final String FONT_URL="src/models/resources/kenvector_future.ttf";
	private static final String REG_BUTTON="-fx-background-color: transparent;-fx-background-image: url('models/resources/Button_1.png');";
	private static final String PRESSED_BUTTON="-fx-background-color: transparent;-fx-background-image: url('models/resources/Button_0.png');";
	public GameButton(String text) {
		name=text;
		setText(text);
		try {
			setFont(Font.loadFont(new FileInputStream(FONT_URL), 23));
		}
		catch(FileNotFoundException e) {
			setFont(Font.font("David", 23));
		}
		setPrefWidth(WIDTH);
		setPrefHeight(HEIGHT);
		this.setTextFill(Color.WHITESMOKE);
		setStyle(REG_BUTTON);
		initListeners();
	}
	private void setRegStyle() {
		setStyle(REG_BUTTON);
		setPrefHeight(HEIGHT);
		setLayoutY(getLayoutY()-4);
	}
	private void setPressedStyle() {
		setStyle(PRESSED_BUTTON);
		setPrefHeight(HEIGHT-4);
		setLayoutY(getLayoutY()+4);
	}
	private void initListeners() {
		setOnMousePressed(e->{
			if(e.getButton().equals(MouseButton.PRIMARY)) 
				setPressedStyle();
		});
			
		setOnMouseReleased(e->{
			if(e.getButton().equals(MouseButton.PRIMARY)) 
				setRegStyle();
		});
		setOnMouseEntered(e->setEffect(new DropShadow()));
		setOnMouseExited(e->setEffect(null));
	}
	public String getName() {
		return name;
	}
}
