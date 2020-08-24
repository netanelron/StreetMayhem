package models;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;
//this class holds 3 static methods to play a sound effect,background music, and to stop background music
public class SoundFX {
	private static AudioClip toPlay;
	private static AudioClip bgm;
	public static void playSound(String soundFile) {
		toPlay=new AudioClip(Paths.get("src/models/sounds/"+soundFile).toUri().toString());
		toPlay.setVolume(0.1);
		toPlay.play();
	}
	public static void playBGM(String soundFile) {
		bgm=new AudioClip(Paths.get("src/displayer/bgm/"+soundFile).toUri().toString());
		bgm.setVolume(0.03);
		bgm.setCycleCount(AudioClip.INDEFINITE);
		bgm.play();
	}
	public static void stopBGM() {
		bgm.stop();
	}
}
