package Billiard;

import java.awt.Color;
import java.awt.Font;

import ch.aplu.jgamegrid.GGTextField;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Timer extends GGTextField{
	private int maxValue;
	private double currentValue;
	private int loopTime;
	
	public Timer(int maxValue, Location location, int loopTime, GameGrid mainWindow) {
		super(mainWindow, String.valueOf(maxValue)+"s", location, true);
	    this.setFont(new Font("SansSerif", Font.BOLD, 17));
		this.setTextColor(Color.green);
		this.maxValue = maxValue;
		this.currentValue = (double) maxValue;
		this.loopTime = loopTime;
		this.setBgColor(Color.DARK_GRAY);
		this.show();
	}
	
	public boolean act() {
		if(this.currentValue >0) {
			this.setText(String.format("Time: %.2fs",this.currentValue));
			this.currentValue = this.currentValue-loopTime*0.001;
			return false;
		}else{
			this.setText("Time Over!");
			return true;
		}
	}
	
	public void increment(int amount) {
		this.currentValue = this.currentValue+amount;
	}
	
	public boolean timeOver() {
		if(this.currentValue <= 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public void reset() {
		this.currentValue = this.maxValue;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return this.isVisible();
	}

}
