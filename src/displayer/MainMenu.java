package displayer;
	
import java.io.File;
import java.io.FileInputStream;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.AnimatedLabel;
import models.GameButton;
import models.MenuSubScene;
import models.SoundFX;
//this class is the main menu of the game
public class MainMenu{
	private static final int HEIGHT=600,WIDTH=800;
	private AnchorPane mainPane;
	private Stage mainStage;
	private Scene scene;
	private MenuSubScene credits;
	private MenuSubScene top10;
	private final static int BUTTON_X=10;
	private final static int BUTTON_Y=150;
	private static int i=0;
	public MainMenu() {
		mainPane=new AnchorPane();
		mainStage=new Stage();
		scene=new Scene(mainPane,WIDTH,HEIGHT);
		mainStage.setScene(scene);
		mainPane.setPadding(new Insets(10));
		mainStage.setTitle("Street Mayhem v0.1");
		mainPane.getChildren().add(new AnimatedLabel("src/models/resources/kenvector_future.ttf","Street Mayhem",(int)(WIDTH/3),4));
		setBackground();
		createAllButtons();
		createSubScenes();
		addClouds();
		mainStage.showingProperty().addListener((observable,oldvalue,newvalue)->{ //every time the menu is re-shown,read the score file again for update
			createTopTenSubScene();
		});
		SoundFX.playBGM("Vengeance.wav");
	}
	public Stage getMainStage() {
		return mainStage;
	}
	private void createAllButtons() {
		createButton("Play");
		createButton("Top Ten");
		createButton("Credits");
		createButton("Quit");
	}
	private void createButton(String text) {
		GameButton button=new GameButton(text);
		button.setLayoutX(BUTTON_X);
		button.setLayoutY(BUTTON_Y+i++*50); //for each button in the menu,set the next one below the previous
		mainPane.getChildren().add(button);
		button.setOnAction(e->{
			switch(button.getName()) {
			case "Play": //if it's the play button,create the game and hide all subscenes
				if(top10.isShown())
					top10.moveSubScene();
				if(credits.isShown())
					credits.moveSubScene();
				SoundFX.stopBGM();
				Game newGame=new Game();
				newGame.CreateGame(mainStage);
				break;
			case "Top Ten": //if it's the top ten button,show/hide the top 10 scoreboard
				if(top10.isShown())
					top10.moveSubScene();
				else {
					if(credits.isShown())
						credits.moveSubScene();
					top10.moveSubScene();
				}
				break;
			case "Credits": //if it's credits button,show/hide credits
				if(top10.isShown())
					top10.moveSubScene();
				credits.moveSubScene();
				break;
			case "Quit": //if it's the quit button,close the game
				mainStage.close();
				break;
			}
		});
	}
	private void setBackground() {
		Image bgImage=new Image("displayer/resources/far-buildings.png");
		BackgroundImage bg=new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, 
				null, new BackgroundSize(WIDTH,HEIGHT,false,false,false,false));
		mainPane.setBackground(new Background(bg));

	}
	private void createSubScenes() {
		credits=new MenuSubScene();
		credits.setContent("credits",readFile("src\\displayer\\resources\\credits.txt"));
		mainPane.getChildren().add(credits);
		createTopTenSubScene();
	}
	private void createTopTenSubScene() {
		top10=new MenuSubScene();
		top10.setContent("Top Ten",readFile("src\\displayer\\resources\\topten.txt"));
		mainPane.getChildren().add(top10);
	}
	private String readFile(String URL) { //simple method to read a file and return the read data as a string
		try {
		File file = new File(URL);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		return  new String(data, "UTF-8");
		}
		catch(Exception e) {
			return new String("Couldn't read file");
		}
	}
	//this methods adds clouds that move over the screen repeatedly
	private void addClouds() {
		Canvas canvas = new Canvas( WIDTH, 100 );
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    Image cloud1 = new Image("models/resources/cloud1.png",100,100,true,false );
	    Image cloud2   = new Image("models/resources/cloud2.png",500,100,true,false );
	    Image cloud3   = new Image("models/resources/cloud3.png",100,100,true,false );
	    gc.drawImage( cloud1,0,0 );
	    gc.drawImage( cloud2,300,0 );
	    gc.drawImage( cloud3,500,0 );
	    PathTransition path=new PathTransition(Duration.seconds(20),new Line(-300, 100, 1600, 100),canvas);
	    path.setCycleCount(Animation.INDEFINITE);
	    path.play();
	    mainPane.getChildren().add(canvas);
	}
}
