package models;

import java.util.Random;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.util.Duration;
//this class defines every enemy in the game
public class Enemy extends AnimatedGameObject {
	static Random rand=new Random();
	private boolean isAirborne;
	public Enemy(boolean isAirborne,Image image, int columns, int count, int offsetX, int offsetY,
			int width, int height, int speed,int startingX,int startingY) {
		super(image, columns, count, offsetX, offsetY, width, height, speed,startingX,startingY);
		this.isAirborne=isAirborne;
		life.set(100);
		setTranslateX(startingX);
		setTranslateY(startingY);
		setLifeVisual();
		animation.play();
	}
	@Override
	public void moveLeft() { //method that once called moves the enemy to the left using a path transition (linear)
		if(getTranslateX()<=30) {
			moveRight();
			return;
		}
		PathTransition path=new PathTransition(Duration.millis(800),new Line(getTranslateX()+16,getTranslateY()+16,getTranslateX()-50,getTranslateY()+16),this);
		path.setCycleCount(1);
		path.play();
	}

	@Override
	public void moveRight() { //method that once called moves the enemy to the left using a path transition (linear)
		if(getTranslateX()>=700) {
			moveLeft();
			return;
		}
		PathTransition path=new PathTransition(Duration.millis(800),new Line(getTranslateX()+16,getTranslateY()+16,getTranslateX()+50,getTranslateY()+16),this);
		path.setCycleCount(1);
		path.play();
		
	}
	public void jumpOrDive() { //method that once called, if the enemy is on the ground makes him jump and if the enemy's airborne he dives down (parabolic movement)
		 PathTransition path=new PathTransition(Duration.millis(800),new QuadCurve(getTranslateX()+16,
				 getTranslateY()+16,getTranslateX()<=30?getTranslateX()+32:getTranslateX()-32,isAirborne?getTranslateY()+80:getTranslateY()-40,
				 getTranslateX()<=30?getTranslateX()+50:getTranslateX()-50,getTranslateY()+16),this);
		 path.setCycleCount(1);
		 path.play();
	}
	public void randMovement() {//method that once called makes the enemy do a random movement
		int num=rand.nextInt(100);
		if(num<20) //20% to move left
			moveLeft();
		else if(num<60) // 40% to move right
			moveRight();
		else if(num<90) //30% to jump/dive
			jumpOrDive();
		//10% to remain still
	}
}
