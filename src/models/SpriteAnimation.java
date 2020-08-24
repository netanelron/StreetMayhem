package models;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
//this class is for every animated game object's animation
public class SpriteAnimation extends Transition {
	private final ImageView images;
	private final int count,columns,width,height;
	private int offsetX,offsetY;
	public SpriteAnimation(Duration dur,ImageView imageView,int columns,int count,int offsetX,int width,int height) {
		this.images=imageView; //holds the image in an image view
		this.count=count; //amount of frames per animation
		this.columns=columns; //amount of columns in the image
		this.height=height;	//size of each frame
		this.width=width;	//size of each frame
		this.offsetX=offsetX;	//first image's offset
		offsetY=0;
		
		setCycleDuration(dur); //set animation's duration to dur
		setCycleCount(Animation.INDEFINITE); //repeat animation
		setInterpolator(Interpolator.LINEAR);
		this.images.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
	}
	public void setOffsetX(int x) {
		this.offsetX=x;
	}
	@Override
	protected void interpolate(double frac) { //in this project I deal with sprite sheets as lines of frames,therefore y=0 and only x changes
		final int index=Math.min((int)Math.floor(count*frac),count-1); //index is animation's current image,goes from 0 to count-1
		final int x=(index%columns)*width+offsetX;
		images.setViewport(new Rectangle2D(x,0,width,height)); //set new image at (x,0) with the given size
	}
}
