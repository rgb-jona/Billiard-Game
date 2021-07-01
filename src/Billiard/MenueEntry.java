package Billiard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

public class MenueEntry extends TextActor{
	private String text;
	private Location location;
	private boolean entered;
	private MenueEntry opp;
	private boolean lastStateVisibility;
	
	public MenueEntry(String text, int x, int y) {
	    super(true, text, Color.white, new Color(0, 0, 0, 0),
	    		new Font("SansSerif", Font.BOLD, 50));
	    this.text = text;
	    this.entered = false;
	    this.location = new Location(x,y);
	}
	
	public MenueEntry(String text, int x, int y, boolean active) {
	    super(true, text, Color.green, new Color(0, 0, 0, 0),
	    		new Font("SansSerif", Font.BOLD, 50));
	    this.text = text;
	    this.entered = true;
	    this.location = new Location(x,y);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setEntered(boolean value) {
		this.entered = value;
	}
	
	public boolean getEnterState() {
		return this.entered;
	}
	
	public void setOpp(MenueEntry opp) {
		this.opp = opp;
	}
	
	public MenueEntry getOpp(){
		return this.opp;
	}
	
	@Override
	public void hide() {
		super.hide();
		this.setLastStateVisibility(false);
	}
	
	@Override
	public void show() {
		super.show();
		this.setLastStateVisibility(true);
	}

	public void setLastStateVisibility(boolean visible) {
		// TODO Auto-generated method stub
		this.lastStateVisibility = visible;
	}
	
}
