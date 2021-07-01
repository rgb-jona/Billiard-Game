package Billiard;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import ch.aplu.jgamegrid.GGKeyListener;
import ch.aplu.jgamegrid.GameGrid;

public class WindowController implements GGKeyListener{
	
	private WindowInterface mainMenue;
	private WindowInterface gameWindow;
	private Map<String,GameGrid> menues = new HashMap<>();
	private WindowInterface currentWindow = null;
	
	public void addGame(WindowInterface game) {
		this.gameWindow = game;
		this.gameWindow.initialize();
	}
	
	public void addMainMenue(WindowInterface mainMenue) {
		this.mainMenue = mainMenue;
		this.mainMenue.initialize();
	}
	
	public void addMenue(String name, GameGrid window) {
		this.menues.put(name, window);
	}
	
	public void inForeground(String name) {
		Point locationOnScreen = null;
		if (this.currentWindow != null) {
			locationOnScreen = this.currentWindow.getPosition();
			this.currentWindow.doPause();
			this.currentWindow.hide();
		}
		if (name.equals("main")) {
			this.currentWindow = this.mainMenue;
		} else if (name.equals("game")) {
			this.currentWindow = this.gameWindow;
		} else {
			// space to place optional menues
			//this.currentWindow = menues.get(name);
		}
		
		if(locationOnScreen != null) {
			this.currentWindow.setPosition((int)locationOnScreen.getX(), (int)locationOnScreen.getY());
		}
		this.currentWindow.show();
		this.currentWindow.doRun();

	}
	
	public void showMainMenue() {
		this.inForeground("main");
	}
	
	public void showGame() {
		this.inForeground("game");
	}

	@Override
	public boolean keyPressed(KeyEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {
			// check on which window the key was pressed
			if (this.currentWindow.getName() == "game") {
				StandardGame game = (StandardGame) this.currentWindow;
				this.inForeground("main");
				Menue menue = (Menue) this.currentWindow;
				if(game.timeOver() == false) {
					menue.changeEntryText("Start Game", "Resume");
					menue.changeEntryText("Help:", "Restart");
				}else{
					menue.changeEntryText("Resume", "Start Game");
					menue.changeEntryText("Restart", "Help:");
					game.restart();
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyReleased(KeyEvent evt) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void resetGame() {
		StandardGame game = (StandardGame) this.gameWindow;
		game.reset();
	}

}
