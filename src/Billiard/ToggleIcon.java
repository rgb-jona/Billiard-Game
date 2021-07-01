package Billiard;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class ToggleIcon  extends Actor{
	private Location location;
	private String name;
	private int currentImageId = 0;
	
	public ToggleIcon(String path, int x, int y) {
		super(path,2);
		this.location = new Location(x,y);
		this.name = path.split("/")[1];
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public void show(int imageId) {
		super.show(imageId);
		this.currentImageId = imageId;
	}
	
	@Override
	public void show() {
		super.show(this.currentImageId);
	}


	@Override
	public boolean isVisible() {
		return super.isVisible();
	}

}
