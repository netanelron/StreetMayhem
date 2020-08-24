package models;

import javafx.scene.image.Image;
//this class is made to define projectiles in game
public class Projectile extends AnimatedGameObject {
	public boolean isRight;
	public String projColor;
	public int velocity;
	public Projectile(Image image, int columns, int count, int offsetX, int offsetY, int width, int height, int speed,int damage,
			int startingX, int startingY) {
		super(image, columns, count, offsetX, offsetY, width, height, speed, startingX, startingY);
		life.set(damage);
	}

	@Override
	public void moveLeft() {
		velocity=-speed;
	}

	@Override
	public void moveRight() {
		velocity=speed;
	}
	public void updateLocation(double elapsedSeconds) { //simple function to update a projectile's location on the X axis given elapsed seconds
	      final double deltaX = elapsedSeconds * velocity;
	      final double oldX =getTranslateX(); 
	      final double newX = Math.max(minX, Math.min(maxX, oldX + deltaX));
	      setTranslateX(newX);
	      if(newX>=maxX||newX<=0) //if out of borders eliminate projectile
	    	  life.set(0);
	}
}
