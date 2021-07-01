package Billiard;

import java.util.HashMap;
import java.util.Map;

import ch.aplu.util.SoundPlayer;

public class SoundEngine {
	private Map<String, String> soundLib = new HashMap<String, String>();
	private boolean muted = false;
	private SoundPlayer menueLoop = new SoundPlayer("assets/sounds/menueMusic.wav");

	public SoundEngine() {
		this.soundLib.put("clickMenue", "assets/sounds/clickMenue.wav");
		this.soundLib.put("clickBall", "assets/sounds/click.wav");
		this.soundLib.put("selectBlop", "assets/sounds/selectBlop.wav");
		this.soundLib.put("borderBounce", "assets/sounds/borderBounce.wav");
		this.soundLib.put("ballInHole", "assets/sounds/ballInHole.wav");
		this.soundLib.put("ballOnTable", "assets/sounds/ballOnTable.wav");
		
		 // error sound played when a sound could not be found
		this.soundLib.put("error", "assets/sounds/error.wav");

	}

	public void playSound(String name) {
		if (!this.muted) {
			try {
				String soundPath = this.soundLib.get(name);
				new SoundPlayer(soundPath).play();
			} catch (Exception e) {
				// catch case that sound is not in soundlib
				e.printStackTrace();
				if(e instanceof java.lang.NullPointerException) {
					String soundPath = this.soundLib.get("error");
					new SoundPlayer(soundPath).play();
				}
			}
		}
	}
	
	public void toggleMenueSound() {
		if (this.menueLoop.isPlaying()) {
			this.menueLoop.stop();
		}else{
			this.menueLoop.playLoop();
			this.menueLoop.setVolume(900);
		}
	}

	public void toggleMuteSound() {
		this.muted = !this.muted;
	}

}
