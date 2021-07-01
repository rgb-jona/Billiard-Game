package Billiard;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class SideBorder extends Actor{
	private Location location;
	private double[] normalVector;
	
	public SideBorder(Location location, double[] normalVector) {
		super(true, "assets/invicSprite_0.png");
		//super(false, "assets/lineSprite312.png");
		//super(true, "assets/kugel_2.gif");
		this.location = location;
		this.normalVector = normalVector;
		//this.setCollisionRectangle(new Point(0,0), xDim, yDim);
		if(Math.abs(this.normalVector[1])>0) {
			this.setCollisionLine(new Point(-156, 0),
								  new Point(156, 0));
		}else{
			this.setCollisionLine(new Point(0, -155),
					  new Point(0, 155));
		}
		//this.setMouseTouchRectangle(new Point(0,0), xDim, yDim);
	}
	
	public SideBorder(Location location, boolean rotation, double[] normalVector) {
		super(true, "assets/invicSprite_0.png");
//		super(true, "assets/lineSprite304.png");
		//super(true, "assets/kugel_2.gif");
		this.location = location;
		this.normalVector = normalVector;
		this.setCollisionLine(new Point(-156, 0), new Point(156, 0));
		//this.setMouseTouchRectangle(new Point(0,0), xDim, yDim);
	}

	public Location getStartPoint() {
		return this.location;
	}

	public double[] getNormalVector() {
		return normalVector;
	}
	
}
