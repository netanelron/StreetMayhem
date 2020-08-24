package models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
//this class is an animated label (changes both color and position smoothly)
public class AnimatedLabel extends Label{
	private final String FONT_URL;
	public AnimatedLabel(String FONT_URL,String text,int offsetX,int offsetY) {
		this.FONT_URL=FONT_URL;
		setLayoutX(offsetX);
		setLayoutY(offsetY);
		setText(text);
		setEffect(new DropShadow());
		try {
			setFont(Font.loadFont(new FileInputStream(this.FONT_URL), 23));
		}
		catch(FileNotFoundException e) {
			setFont(Font.font("David", 23));
		}
		final long startNanoTime=System.nanoTime();
		new AnimationTimer() {
			@Override
			public void handle(long currentNanoTime) {
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				setTextFill(Color.color(1, 0, 1*Math.abs(Math.sin(t))));
				setLayoutY(4*Math.sin(t));
			}}.start();
	}
}
