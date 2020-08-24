package models;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameCharacter extends AnimatedGameObject {
	    private boolean isRight;
	    public boolean canShoot=true;
	    private double gravity=500;
	    private final DoubleProperty HorizontalVelocity = new SimpleDoubleProperty();
	    private double verticalVelocity;
	    public IntegerProperty mana=new SimpleIntegerProperty();
	    public GameCharacter() {
	    	super(new Image("models/resources/player_sheet1.png"), 24, 3, 0, 0, 40, 40, 50,0,450);
	    	life.set(100);
	    	mana.set(100);
	    	setLifeVisual();
	    	setManaVisual();
	    	HorizontalVelocity.addListener((observable, oldvalue, newvalue)->{
	    		if(newvalue.doubleValue()>0&&onGround)
	    			animation.setOffsetX(840);
	    		else if(newvalue.doubleValue()<0&&onGround)
	    			animation.setOffsetX(720);
	    		animation.play();
	    	});
	    }
	    @Override
		public void moveLeft() {
			isRight=false;
			HorizontalVelocity.set(-speed);
		}
	    @Override
		public void moveRight() {
			isRight=true;
			HorizontalVelocity.set(speed);
		}
		public void jump() {
			if(onGround) {
				SoundFX.playSound("PP_20.wav");
				verticalVelocity=-15*speed;
				animation.play();
				if(isRight) 
					animation.setOffsetX(600);
				else 
					animation.setOffsetX(480);
			}
			
		}
		public void stopMoving() {
			HorizontalVelocity.set(0);
		}
		private void idle() {
			animation.play();
			if(isRight) 
				animation.setOffsetX(360);
			else 
				animation.setOffsetX(240);
		}
	    @Override
	    public String toString() {
	    	return "Player";
	    }
	    public void updateLocation(double elapsedSeconds) {
		      final double deltaX = elapsedSeconds * HorizontalVelocity.get();
		      final double oldX =getTranslateX(); 
		      final double newX = Math.max(minX, Math.min(maxX, oldX + deltaX));
		      verticalVelocity=verticalVelocity+gravity*elapsedSeconds;
		      final double deltaY=elapsedSeconds *(verticalVelocity+gravity);
		      final double oldY = getTranslateY();
		      final double newY = Math.max(minY, Math.min(maxY, oldY + deltaY));
		      onGround=newY==oldY;
		      if(onGround&&oldX==newX&&canShoot)
		    	  idle();
		      setTranslateX(newX);
		      setTranslateY(newY);
	    }
	    public void flinch() {
	    	SoundFX.playSound("hurt_017.wav");
			FadeTransition ft=new FadeTransition(Duration.millis(500),this);
			ft.setCycleCount(4);
			ft.setFromValue(0.3);
			ft.setToValue(1.0);
			ft.play();
	    }
	    public Projectile shoot(String type) {
	    	Projectile projectile;
	    	if(type.equals("RED")) {
	    		mana.set(mana.get()-30);
	    		SoundFX.playSound("explosion_wide_49.wav");
	    		projectile=new Projectile(new Image("models/resources/red_projectile_sheet.png"), 6, 3, 0, 0, 32, 16, 150,50,0,(int)getTranslateY()+15);
	    	}
	    	else { //BLUE
	    		mana.set(mana.get()-30);
	    		SoundFX.playSound("explosion_big_19.wav");
	    		projectile=new Projectile(new Image("models/resources/blue_projectile_sheet.png"), 6, 4, 0, 0, 32, 16,400,30,0,(int)getTranslateY()+15);
	    	}
	    	projectile.projColor=type;
	    	projectile.animation.play();
	    	canShoot=false;
	    	projectile.isRight=isRight;
	    	animation.stop();
	    	animation.setCycleCount(1);
	    	animation.setRate(2);
	    	animation.play();
	    	if(isRight) { //shoot to the right
	    		animation.setOffsetX(120);
	    		projectile.animation.setOffsetX(type.equals("RED")?96:128);
	    		projectile.setTranslateX(getTranslateX()+15);
	    		projectile.moveRight();
	    	}
	    	else { //shoot to the left
	    		animation.setOffsetX(0);
	    		projectile.animation.setOffsetX(0);
	    		projectile.setTranslateX(getTranslateX()-15);
	    		projectile.moveLeft();
	    	}
	    	//when the shoot animation's finished return to normal animation settings
	    	animation.setOnFinished(e->{
	    		canShoot=true;
	    		animation.setCycleCount(Animation.INDEFINITE);
	    		animation.setRate(1);
	    	});
	    	return projectile; //return the projectile so we can maintain it
	    }
	    private void setManaVisual() {
	    	Rectangle outerBar=new Rectangle(10,2.5,20,2.5);
			outerBar.setFill(Color.BLACK);
			Rectangle ManaBar = new Rectangle(10, 2.5, 20, 2.5);
			ManaBar.setFill(Color.CORNFLOWERBLUE);
			mana.addListener((observable, oldvalue, newvalue)->{
				ManaBar.setWidth(newvalue.intValue()*0.2);
			 });
			getChildren().addAll(outerBar,ManaBar);
		}
}
