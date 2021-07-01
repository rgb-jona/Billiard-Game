package Billiard;

import java.awt.Point;

public class CatchedBall {
	private Ball ball;
	private double beginDistanceToHole;
	private Point holeCenter;
	
	public CatchedBall(Ball ball, Point holeCenter) {
		this.ball = ball;
		this.beginDistanceToHole = Math.sqrt(Math.pow(holeCenter.getX()-ball.getPixelLocation().getX(), 2)
				+Math.pow(holeCenter.getY()-ball.getPixelLocation().getY(), 2));
		this.holeCenter = holeCenter;
		this.ball.setVelocityVector((holeCenter.getX()-ball.getPixelLocation().getX())/this.beginDistanceToHole,
				(holeCenter.getY()-ball.getPixelLocation().getY())/this.beginDistanceToHole);
	}
	
	private void setNextLocation() {
		// calculate the new position of the ball
		double newX = this.ball.getCurrentPosition().get(0)+this.ball.getVelocityVector().get(0)*this.ball.getVelocity();
		double newY = this.ball.getCurrentPosition().get(1)+this.ball.getVelocityVector().get(1)*this.ball.getVelocity();
		
		// calculate the new distance to the hole center
		double newDistanceToHoleCenter = Math.sqrt(Math.pow(holeCenter.getX()-newX, 2)
				+Math.pow(holeCenter.getY()-newY, 2));
		
		// set ball location to hole center, when the distance to the whole center is shorter than the distance in next step,
		// which prevents the ball from not reaching hole center and therefore not disappearing
		if (newDistanceToHoleCenter<= this.ball.getVelocity()) {
			this.ball.setCurrentPosition(this.holeCenter.getX(), this.holeCenter.getY());
		}else{
			this.ball.setCurrentPosition(newX, newY);
		}
		ball.applyCurrentPosition();
	}
	
	public boolean  nextCatchStep() {
		// this decides whether the ball needs to move to the hole center or has already reached it and should disappear
		if(!this.ball.getPixelLocation().equals(holeCenter)) {
			this.setNextLocation();
			return true;
		}else{
			this.ball.catched();
			return false;
		}
	}

}
