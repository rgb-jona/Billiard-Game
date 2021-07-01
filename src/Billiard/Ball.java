package Billiard;

import java.awt.Point;
import java.util.ArrayList;

import ch.aplu.jgamegrid.Actor;

public class Ball extends Actor{
	private Point initialPoint;
	private double velocity;
	private ArrayList<Double> velocityVector;  // direction of velocity x,y normed to 1
	private ArrayList<Double> currentPosition = new ArrayList<>(2);
	public static SoundEngine soundEngine;
	
	public static void addSoundEngine(SoundEngine soundEngine) {
		Ball.soundEngine = soundEngine;
	}
	
	public Ball(String spritePath, Point initialPoint) {
		super(true,spritePath);
		this.initialPoint = initialPoint;
		this.currentPosition.add(initialPoint.getX());
		this.currentPosition.add(initialPoint.getY());
		this.velocity = 0;
	}

	
	public Point getInitialPoint() {
		return initialPoint;
	}
	
	public void act() {
			this.calculateNewPosition();
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public double getVelocity() {
		return this.velocity;
	}
	
	public void calculateNewPosition() {
		if(this.velocity>0.05) {
			// apply drag during the movement of the ball due to a self trial and error created drag formulq which applies
			// more drag when the ball is slower
			this.velocity = this.velocity*(-1/(Math.pow(this.velocity+1, 2)*7)+0.996);
			//System.out.printf("Velocity: %.2f\n", this.velocity);
			this.currentPosition.set(0,this.currentPosition.get(0)+this.velocityVector.get(0)*this.velocity);
			this.currentPosition.set(1,this.currentPosition.get(1)+this.velocityVector.get(1)*this.velocity);
			this.setPixelLocation(new Point(this.currentPosition.get(0).intValue(),this.currentPosition.get(1).intValue()));
		}else {
			this.velocity = 0;
			this.setVelocityVector(0, 0);
		}
		
	}

	public ArrayList<Double> getVelocityVector() {
		return this.velocityVector;
	}
	
	public ArrayList<Double> getCurrentPosition() {
		return this.currentPosition;
	}
	
	public void setCurrentPosition(double x , double y) {
		this.currentPosition.set(0, x);
		this.currentPosition.set(1, y);
	}
	
	public void applyCurrentPosition() {
		this.setPixelLocation(new Point(this.currentPosition.get(0).intValue(),this.currentPosition.get(1).intValue()));
	}

	public void setVelocityVector(double x, double y) {
		ArrayList<Double> velocity = new ArrayList<Double>();
		double norm = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		velocity.add(x/norm);
		velocity.add(y/norm);
		this.velocityVector = velocity;
	}
	
	public void catched() {
		super.hide();
		Ball.soundEngine.playSound("ballInHole");
		this.setActEnabled(false);
	}
	
	public void resetPosition() {
		this.setCurrentPosition(initialPoint.getX(), initialPoint.getY());
		this.applyCurrentPosition();
		this.show();
		this.setVelocity(0);
		this.setActEnabled(true);
	}
}
