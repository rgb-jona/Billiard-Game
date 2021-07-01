package Billiard;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class Menue extends GameGrid implements GGMouseTouchListener, WindowInterface{

	private SoundEngine soundEngine;
	private WindowController controller;
	private MenueEntry[][] entries;

	public Menue(SoundEngine soundEngine, WindowController controller) {
		super(160, 88, 5, java.awt.Color.red, "assets/PoolTable.png", false);
		this.soundEngine = soundEngine;
		this.controller = controller;
		this.addKeyListener(controller);
		this.setName("main");
	}

	@Override
	public void initialize() {
		this.setTitle("Menue");
		this.entries = new MenueEntry[3][2];

		// create and add menue entry actors
		this.entries[0] = createMenueEntry("Start Game",1);
		this.entries[1] = createMenueEntry("Help:",2);
//		this.entries[2] = createMenueEntry("Options",3);
		this.entries[2] = createMenueEntry("Exit",5);

		// create Icons to toggle sound effects and music
		createToggleIcon("assets/music.png", 120, 70);
		createToggleIcon("assets/sound.png",140,70);
		
		// add help text
		GGTextField helpLine1 = new GGTextField(this, "Get as many balls as posible into the holes, until", new Location(21,44), false);
		helpLine1.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 25));
		helpLine1.setTextColor(WHITE);
		helpLine1.show();
		GGTextField helpLine2 = new GGTextField(this, "the time is over. Every scored ball gives 10s extra time.", new Location(13,50), false);
		helpLine2.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 25));
		helpLine2.setTextColor(WHITE);
		helpLine2.show();
		GGTextField helpLine3 = new GGTextField(this, "Use left-hold and drag to aim and shoot the white ball,", new Location(14,56), false);
		helpLine3.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 25));
		helpLine3.setTextColor(WHITE);
		helpLine3.show();
		GGTextField helpLine4 = new GGTextField(this, "right-click to cancel aiming.", new Location(48,62), false);
		helpLine4.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 25));
		helpLine4.setTextColor(WHITE);
		helpLine4.show();

		// play sound for menue   rightclick to cancel aiming.
		this.soundEngine.toggleMenueSound();
	}

	@Override
	public void mouseTouched(Actor actor_, GGMouse mouse, Point spot) {
		if(actor_ instanceof MenueEntry) {
			MenueEntry actor = (MenueEntry) actor_;
			String text = (actor.getText());
			if(mouse.getEvent() == GGMouse.enter && !actor.getEnterState()) {
				this.soundEngine.playSound("clickMenue");
				actor.hide();
				actor.getOpp().setEntered(true);
				actor.getOpp().show();
			}
			else if (mouse.getEvent() == GGMouse.leave && actor.getEnterState()) {
				actor.hide();
				actor.setEntered(false);
				actor.getOpp().show();
			}
			else if (mouse.getEvent() == GGMouse.lClick) {
				this.soundEngine.playSound("selectBlop");
				if (text == "Exit") {
					System.exit(0);
				}else if(text == "Start Game" || text == "Resume") {
					controller.inForeground("game");
				}else if(text =="Restart") {
					controller.resetGame();
					controller.inForeground("game");
				}
			}
		} else if(actor_ instanceof ToggleIcon) {
			ToggleIcon iconActor = (ToggleIcon) actor_;
			if (mouse.getEvent() == GGMouse.lClick) {
				this.soundEngine.playSound("clickMenue");
				if(iconActor.getName().equals("music.png")) {
					this.soundEngine.toggleMenueSound();
				}else if (iconActor.getName().equals("sound.png")) {
					this.soundEngine.toggleMuteSound();
				}
				iconActor.showNextSprite();
			}
		}
		this.refresh();
	}
	
	public void changeEntryText(String oldText, String newText) {
		for(int i = 0;i<this.entries.length;i++) {
			if(this.entries[i][0].getText().equals(oldText)) {
				this.entries[i][0].hide();
				this.entries[i][1].hide();
				this.entries[i] = createMenueEntry(newText,i+1);
			}
		}
	}

	private void addMenueEntry(MenueEntry newEntry) {
		newEntry.addMouseTouchListener(this, GGMouse.leave | GGMouse.enter | GGMouse.lClick);
		this.addActor(newEntry, newEntry.getLocation());
		newEntry.setMouseTouchRectangle(new Point(0 + newEntry.getTextWidth()/2, 0), newEntry.getTextWidth(),
				newEntry.getTextHeight()-15);
	}
	
	private MenueEntry[] createMenueEntry(String text, int rowNumber) {
		MenueEntry entry = new MenueEntry(text, 0, 0);
		MenueEntry entryOpp = new MenueEntry(text, 0, 0, true);
		addMenueEntry(entry);
		addMenueEntry(entryOpp);
		// calculate location from window size
		entry.setPixelLocation(new Point(this.getWidth()/2-entry.getTextWidth()/2, 50+rowNumber*entry.getTextHeight()));
		entryOpp.setPixelLocation(new Point(this.getWidth()/2-entryOpp.getTextWidth()/2, 50+rowNumber*entryOpp.getTextHeight()));
		entryOpp.hide();
		entry.setOpp(entryOpp);
		entryOpp.setOpp(entry);

		MenueEntry[] returnArray = {entry,entryOpp};
		return returnArray;
	}
	
	private void createToggleIcon(String path, int x, int y) {
		ToggleIcon icon = new ToggleIcon(path, x, y);
		addIconEntry(icon);
	}

	private void addIconEntry(ToggleIcon newIcon) {
		newIcon.addMouseTouchListener(this, GGMouse.lClick);
		this.addActor(newIcon, newIcon.getLocation());
		newIcon.setMouseTouchRectangle(new Point(0, -15), 80,
				100);
	}
	
	@Override
	public void hide() {
		super.hide();
	}
	
	@Override
	public void doRun() {
		super.doRun();
	}
	
	@Override
	public boolean isRunning() {
		return super.isRunning();
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
	}
	
	@Override
	public String getName() {
		return super.getName();
	}
	
	@Override
	public void doPause() {
		super.doPause();
	}
	
	@Override
	public boolean isPaused() {
		return super.isPaused();
	}
	
	@Override
	public Point getPosition() {
		return super.getPosition();
	}
	
	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x,y);
	}
}
