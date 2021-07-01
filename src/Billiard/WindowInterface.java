package Billiard;

import java.awt.Point;

public interface WindowInterface {
	
	public void initialize();
	public void hide();
	public void doRun();
	public void show();
	public boolean isRunning();
	public String getName();
	public void setName(String name);
	public void doPause();
	public boolean isPaused();
	public Point getPosition();
	public void setPosition(int x, int y);
}
