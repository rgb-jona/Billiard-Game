package Billiard;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Hole extends Actor{
	private Location startPoint;
	private CatchedBall catchedBall = null;
	
	public Hole(Location location, int circle) {
		super(false,"assets/invicSprite_0.png");
		//super(false,"assets/hole26.png");
		//super(false,"assets/lineSprite312.png");
		this.startPoint = location;
		//this.setMouseTouchCircle(new Point(0,0), circle*3);
	}
	
	public Location getStartPoint() {
		return startPoint;
	}
	
	public void addBallToCollect(Ball ball) {
		// set ball as new catched ball
		this.catchedBall = new CatchedBall(ball,this.getPixelLocation());
	}
	
	public void act() {
		// check if a catched ball exists and check if there is a next step to execute,
		// otherwise set catched ball to null since there is no ball which has been catched
		if(this.catchedBall != null && !this.catchedBall.nextCatchStep()) {
			this.catchedBall = null;
		}
	}
	

}
