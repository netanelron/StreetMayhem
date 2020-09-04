package displayer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AnimatedGameObject;
import models.AnimatedLabel;
import models.Enemy;
import models.GameButton;
import models.GameCharacter;
import models.Projectile;
import models.SoundFX;

public class Game {
	private static final int HEIGHT=600,WIDTH=800;
	private static final String[] enemyArr=new String[] {"models/resources/bat_sheet.png","models/resources/slime1_sheet.png"
			,"models/resources/slime2_sheet.png","models/resources/slime3_sheet.png"};
	private static final Random rand=new Random();
	private AnchorPane gamePane;
	private Stage gameStage;
	private Scene gameScene;
	private Stage menuStage;
	private GameCharacter player;
	private double invulTime=0;
	private double timeCounter=0;
	private List<Projectile> projectileList;
	private List<Enemy> enemyList;
	private boolean gameSet=true;
	private AnimationTimer gameTime;
	private IntegerProperty score=new SimpleIntegerProperty();
	private String name="A";
	private final LongProperty lastUpdateTime = new SimpleLongProperty();
	private AnimatedLabel scoreLabel;
	public Game() {
		initGame();
		initKeyListeners();
		Platform.runLater(()->{
			gamePause();
		});	
	}
	private void initGame() {
		score.set(0);
		scoreLabel=new AnimatedLabel("src/models/resources/kenvector_future.ttf","Score:"+score.getValue().toString(),330,4);
		score.addListener((observable, oldvalue, newvalue) ->{
			scoreLabel.setText("Score:"+score.getValue().toString());
		});
		SoundFX.playBGM("Mission.wav");
		projectileList=new ArrayList<Projectile>();
		enemyList=new ArrayList<Enemy>();
		gamePane=new AnchorPane();
		gamePane.getChildren().add(scoreLabel);
		gameScene=new Scene(gamePane,WIDTH,HEIGHT);
		gameStage=new Stage();
		setCharacter();
		setEnemies();
		gameStage.setScene(gameScene);
		setBackground();
		///main time loop for the game\\\
		gameTime=new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
			    if (lastUpdateTime.get() > 0) {
			    	final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
			    	player.updateLocation(elapsedSeconds); 
					update(elapsedSeconds);
			    }
				lastUpdateTime.set(timestamp); 
			}
		};
		gameTime.start();
	}
	public void update(double elapsedSeconds ) {
		invulTime=invulTime==0?0:invulTime+0.016;
		timeCounter=timeCounter>=1?0:timeCounter+0.016;
		projectileList.forEach(p->{
			p.updateLocation(elapsedSeconds);
		});
		if(!enemyList.isEmpty()) { //run through all enemies
			enemyList.forEach(enemy->{
				 if(enemy.getBoundsInParent().intersects(player.getTranslateX()+8,player.getTranslateY()+8,16,16)) { //if enemy hits player's hit box (less clunky than player's bounds)
						if(invulTime==0) {//start counting once hit and set player invincible
							displayDamage("RED",25,player);
							displayHealth(25,player);
							player.flinch();
							invulTime+=0.016;
						}
					}
				 projectileList.forEach(p->{ //run through all projectiles for each enemy to see if any of them hits the enemy
					 if(p.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
						 int criticalChance=rand.nextInt(100);
						 int damage=p.projColor=="RED"?p.life.get():(criticalChance<=25?2*p.life.get():p.life.get());
						 if(damage>0) {
							 displayDamage("VIOLET",damage,enemy);
							 displayHealth(damage,enemy);
						 }
						 p.life.set(0);
						}
				 });
	    		 if(timeCounter>=1)
	    			enemy.randMovement(); 
			});
		}
		else  //there are no enemies,spawn some more
			setEnemies(); 
		if(invulTime>=2) { //it has been 2 seconds since player got hit,reset invulnerability timer and set his effect to null
			invulTime=0;
		}
		if(timeCounter>=1 && player.mana.get()<=90) //mana regeneration
			player.mana.set(player.mana.get()+25);
		//filter enemy and projectile lists to get rid of dead objects
		enemyList=enemyList.stream().filter(e->e.life.get()>0).collect(Collectors.toList());
		projectileList=projectileList.stream().filter(p->p.life.get()>0).collect(Collectors.toList());
	}
	public void initKeyListeners() {
			gameScene.setOnKeyPressed(e->{
				if(gameSet) {
					switch(e.getCode()) {
					case RIGHT:
						player.moveRight();
						break;
					case LEFT:
						player.moveLeft();
						break;
					case SPACE:
						player.jump();
						break;
					case A: case S:
						if(player.mana.get()>=30 && player.canShoot) {
							Projectile p=player.shoot(e.getCode().equals(KeyCode.A)?"RED":"BLUE");
							projectileList.add(p);
							gamePane.getChildren().add(p);
							//this is a simple listener for when the projectile's life is 0 (out of bounds/hit),create an explosion
							p.life.addListener((observable,oldvalue,newvalue)->{
								if(newvalue.intValue()==0) {
									p.animation.stop();
									gamePane.getChildren().remove(p);
									String projImgURL=p.projColor.equals("RED")?"models/resources/red_hit_sheet.png":"models/resources/blue_hit_sheet.png";
									displayExplosion(200,projImgURL,6,3,p.isRight?0:96,0,32,32,p.getTranslateX()+(p.isRight?16:-16),p.getTranslateY()-8);
								}	
							});
						}
						break;
					case ESCAPE:
						gamePause();
						break;
					default:
						break;
						}
				}
			});
			gameScene.setOnKeyReleased(e->{
				switch(e.getCode()) {
				case RIGHT:
					player.stopMoving();
					break;
				case LEFT:
					player.stopMoving();
					break;
				default:
					break;
					}
			});	
	}
	//this method switches between menu and game stages
	public void CreateGame(Stage menuStage) {
		this.menuStage=menuStage;
		this.menuStage.close();
		gameStage.show();
	}
	//this method sets the game character in the game
	private void setCharacter() {
		player=new GameCharacter();
		//add a listener to player's life so when it's dead the game's over
		player.life.addListener((observable, oldvalue, newvalue)->{
			if(newvalue.intValue()<=0)
				gameOver();
		});
		gamePane.getChildren().add(player);
	}
	//this method sets enemies in the game
	private void setEnemies() {
		int num;
		SoundFX.playSound("Alarm_01.wav");
		for(int i=0;i<5+score.intValue()/2;i++) {
			num=rand.nextInt(4);
			//enemies vary between 0-3 values,where 0 is the bat enemy and rest are slimes
			Enemy enemy=new Enemy(num==0,new Image(enemyArr[num]),4,4,0,0,32,32,30,200+rand.nextInt(400),num==0?400:455);
			gamePane.getChildren().add(enemy);
			enemyList.add(enemy);
			//add a listener to enemy's life so when it's dead score goes up and the enemy's removed
			enemy.life.addListener((observable, oldvalue, newvalue)->{
				if(newvalue.intValue()<=0) {
					score.set(score.intValue()+1);
				    gamePane.getChildren().remove(enemy);
				}
			});
		}
	}
	//this method sets the background of the game
	private void setBackground() {
		Image bgImage=new Image("models/resources/game_bg.png");
		BackgroundImage bg=new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(WIDTH,HEIGHT,false,false,false,false));
		gamePane.setBackground(new Background(bg));
	}
	//this method removes all game objects and stops the time run then calls gameOverMessage()
	private void gameOver() {
		gameSet=false;
		List<AnimatedGameObject> tempList=new ArrayList<AnimatedGameObject>();
		tempList.add(player);
		tempList.addAll(projectileList);
		tempList.addAll(enemyList);
		gamePane.getChildren().removeAll(tempList); //remove all animated game objects from the game
		gameTime.stop(); //stop game time
		gameOverMessage("Game Over!\n Please enter your name:"); //pop up the game over message
	}
	//this method reads a file,sorts it and rewrites the 10 first sorted elements
	private void updateScoreFile() {
		try {
			File file=new File("src\\displayer\\resources\\topten.txt");
			Scanner s=new Scanner(file);
			class NamedScore implements Comparable<NamedScore> { //simple class that holds a name and a score,comparable by score
				int score;
				String name;
				public NamedScore(int score,String name) {
					StringBuilder str=new StringBuilder("");
					this.score=score;
					str.append(name);
					for(int i=0;i<20-name.length();i++)
						str.append("   ");
					this.name=str.toString();
				}
				@Override
				public int compareTo(NamedScore o) {
					return this.score>o.score?1:this.score<o.score?-1:this.name.compareTo(o.name);
				}
			}
			List<NamedScore> scoreList=new ArrayList<NamedScore>();
			while(s.hasNext()) { //read all the file and add each <name,score> to the list
				StringBuilder builder=new StringBuilder("");
				while(s.hasNext()&&!s.hasNextInt())
					builder.append(s.next());
				String tmpName=builder.toString();
				int tmpScore=s.nextInt();
					scoreList.add(new NamedScore(tmpScore,tmpName));
			}
			s.close();
			scoreList.add(new NamedScore(score.intValue(),name)); //add new score and name to list
			Collections.sort(scoreList); //sort the list
			//clear the file
			FileWriter myWriter = new FileWriter("src\\displayer\\resources\\topten.txt");
			myWriter.write("");
			myWriter.close();
			//rewrite file with top ten scores
			myWriter = new FileWriter("src\\displayer\\resources\\topten.txt",true);
			for(int i=0;i<10&&i<scoreList.size();i++) {
				myWriter.write(String.format("%s\t%d\n",scoreList.get(scoreList.size()-1-i).name,scoreList.get(scoreList.size()-1-i).score));
			}
			myWriter.close();
		}
		catch (Exception e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	//this method takes a text and a node list and displays a popup message with given text and nodes as children
	private Stage popUpMessage(String text,List<Node> nodeList) { 	
		Stage stage=new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		VBox pane=new VBox();
		pane.setPadding(new Insets(5));
		pane.setStyle("-fx-background-color: BLACK;");
		//set label,text field and exit button
		Label label=new Label(text);
		label.setFont(Font.font("Impact",23));
		label.setEffect(new Lighting());
		label.setTextFill(Paint.valueOf("WHITE"));
		pane.getChildren().add(label);
		pane.getChildren().addAll(nodeList);
		Scene scene=new Scene(pane);
		stage.setScene(scene);
		stage.show();
		return stage;
	}
	//this method displays a window with an option to enter name and a return to menu button
	private void gameOverMessage(String text) {
		List<Node> list=new ArrayList<>();
		GameButton exitButton=new GameButton("Return to menu");
		exitButton.setPrefWidth(300);
		TextField txt=new TextField();
		txt.setFont(Font.font("Impact",23));
		txt.setStyle("-fx-background-color: GREY;");
		list.add(txt);
		list.add(exitButton);
		Stage stage=popUpMessage(text,list);
		exitButton.setOnAction(e->{ //upon pressing the button,stop the game and return to menu
			SoundFX.stopBGM();
			gameStage.close();
			SoundFX.playBGM("Vengeance.wav");
			name=txt.getText();
			name=name.trim().isEmpty()?"Unknown":name;
			updateScoreFile();
			stage.hide();
			menuStage.show();
		});
	}
	//this method pauses the game and displays a window with controls and return button
	private void gamePause() {
		List<Node> list=new ArrayList<>();
		GameButton returnButton=new GameButton("Return to game");
		returnButton.setPrefWidth(300);
		gameTime.stop();
		lastUpdateTime.set(0);
		list.add(returnButton);
		Stage stage=popUpMessage("\t\t  GAME PAUSED\n Controls:\n 'A'-Red Flame\n(30 MP,50 DMG,SLOW) ,\n "
				+ "'S'-Blue Flame\n(30 MP,25 DMG, 25% CRIT,FAST) ,\n 'SPACE'-Jump ,\n ARROWS- Move Left/Right , \n ESC-Pause Game",list);
		returnButton.setOnAction(e->{ //resume to game and hide this pane
			gameTime.start();
			stage.hide();
		});
	}
	
	//this segment consists of three methods created solely to demonstrate different approaches to multi-threading in javafx-as requested by my advisor
	
	
	//this method displays damage taken by node in an animation using multi-threading
	private void displayDamage(String color,int dmg,Pane node) {
		new Thread(()->{
			Label label=new Label(String.format("%d", dmg));
			label.setTextFill(Paint.valueOf(color));
			int miliTime=0;
			Platform.runLater(()->{
				gamePane.getChildren().add(label);
			});
			while(miliTime<=400) {
				label.setFont(Font.font("Impact",((miliTime+100)/100)*2+10));//increase size according to time passed
				label.setOpacity(label.getOpacity()-0.02);
				Platform.runLater(()->{
					label.setTranslateY(node.getTranslateY()-30);
					label.setTranslateX(node.getTranslateX()+10);
				});
				//count the time
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {}
				miliTime+=16; 
			}	
			Platform.runLater(()->{
				gamePane.getChildren().remove(label);
			});
		}).start();
	}
	//this method displays an explosion animation in a certain location using multi-threading
	private void displayExplosion(int miliSeconds,String sprite_URL,int columns,int count,int offsetX,int offsetY,int height,int width,double startX,double startY) {
		ImageView img=new ImageView(new Image(sprite_URL));
		Task<Void> task=new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				img.setViewport(new Rectangle2D(offsetX ,offsetY, height, width));
				img.setTranslateX(startX);
				img.setTranslateY(startY);
				Platform.runLater(()->{
					gamePane.getChildren().add(img);
				});
				for(int i=0;i<miliSeconds;i+=16) {
					double frac=(double)i/miliSeconds;
					final int index=Math.min((int)Math.floor(count*frac),count-1); //index is animation's current image,goes from 0 to count-1
					final int x=(index%columns)*width+offsetX;
					img.setViewport(new Rectangle2D(x,0,width,height)); //set new image at (x,0) with the given size
					Thread.sleep(16);
				}
				return null;
			}	
		};
		task.setOnSucceeded(e->{
			gamePane.getChildren().remove(img);
		});
		new Thread(task).start();
	}
	//this method gradually decreases an object's health based on the damage taken
	private void displayHealth(int damage,AnimatedGameObject obj) {
		Task<Void> task=new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for(int i=0;i<damage;i+=5) {
					updateProgress(i,damage);
					Thread.sleep(16);
				}
				return null;
			}	
		};
		task.progressProperty().addListener((observable,oldvalue,newvalue)->{
			synchronized(task) {
				if(obj.life.get()>0)
					obj.life.set(obj.life.get()-5);
			}
		});
		new Thread(task).start();
	}
	
}
