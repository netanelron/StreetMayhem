package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
//this abstract class is used as a parent for all animated game objects in the game
public abstract class AnimatedGameObject extends Pane {
	protected final Image image;
	public final ImageView imageView;
	public SpriteAnimation animation;
	protected final int columns, count, offsetX, offsetY, width, height, speed;
	protected boolean onGround;
	//defines the borders of the game
	protected final int minX = 0;
	protected final int maxX = 800;
	protected final int minY = 0;
	protected final int maxY = 450;
	public IntegerProperty life = new SimpleIntegerProperty();
	public boolean alive = true;
	public AnimatedGameObject(Image image, int columns, int count, int offsetX, int offsetY, int width, int height,
			int speed, int startingX, int startingY) {
		this.image = image;
		this.columns = columns;
		this.count = count;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
		this.speed = speed;
		imageView = new ImageView(image);
		setTranslateX(startingX);
		setTranslateY(startingY);
		//create an animation object with given sprite sheet
		animation = new SpriteAnimation(Duration.millis(1000), imageView, columns, count, offsetX, width, height);
		getChildren().add(imageView);
	}
	protected void setLifeVisual() { //this method sets a visual life bar that reacts to the object's life
		Rectangle outerBar=new Rectangle(10,0,20,2.5);
		outerBar.setFill(Color.BLACK);
		Rectangle lifeBar = new Rectangle(10, 0, 20, 2.5);
		lifeBar.setFill(Color.RED);
		life.addListener((observable, oldvalue, newvalue)->{
		 lifeBar.setWidth(newvalue.intValue()*0.2);
		 });
		this.getChildren().addAll(outerBar,lifeBar);
	}

	public abstract void moveLeft();

	public abstract void moveRight();
}
