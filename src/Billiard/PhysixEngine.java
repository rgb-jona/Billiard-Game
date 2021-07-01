package Billiard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGActorCollisionListener;

public class PhysixEngine implements GGActorCollisionListener {
	private Timer timer;
	private SoundEngine soundEngine;
	
	public PhysixEngine(Timer timer, SoundEngine soundEngine) {
		this.timer = timer;
		this.soundEngine = soundEngine;
	}
	
	private double norm(double x, double y) {
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}

	@Override
	public int collide(Actor actor1_, Actor actor2_) {
		//System.out.println("collide");
		if (actor1_ instanceof Ball && actor2_ instanceof Ball) {
			// catch case that the white ball is placed on an other ball (when the white ball can be placed freehanded) when it has been shot into a hole
			if(actor1_ instanceof WhiteBall) {
				WhiteBall whiteBall = (WhiteBall) actor1_;
				if(whiteBall.isFreshPlaced() && whiteBall.getVelocity() == 0) {
					Ball actor = (Ball) actor2_;
					double xDist = whiteBall.getCurrentPosition().get(0)-actor.getCurrentPosition().get(0);
					double yDist = whiteBall.getCurrentPosition().get(1)-actor.getCurrentPosition().get(1);
					whiteBall.setCurrentPosition(whiteBall.getCurrentPosition().get(0)+10.0, whiteBall.getCurrentPosition().get(1)+10.0);
					whiteBall.applyCurrentPosition();
					return 0;
				}
			}
			
			// case when 2 balls are colliding
			Ball actor1 = (Ball) actor1_;
			Ball actor2 = (Ball) actor2_;
			ArrayList<Double> actor1Position = actor1.getCurrentPosition();
			ArrayList<Double> actor2Position = actor2.getCurrentPosition();
			double actor1Velocity = actor1.getVelocity();
			double actor2Velocity = actor2.getVelocity();
			ArrayList<Double> actor1Direction = actor1.getVelocityVector();
			ArrayList<Double> actor2Direction = actor2.getVelocityVector();
			
			// play sound for the collision
			this.soundEngine.playSound("clickBall");

			// calculate vector from active to passive actor in the collision
			double collisionVectorNorm = Math.sqrt(Math.pow(actor2Position.get(0) - actor1Position.get(0), 2)+
					Math.pow(actor2Position.get(1) - actor1Position.get(1), 2));
			List<Double> collisionVector = Arrays.asList((actor2Position.get(0) - actor1Position.get(0))*1/collisionVectorNorm,
					(actor2Position.get(1) - actor1Position.get(1))*1/collisionVectorNorm);
			
			// set actor 1 apart from actor 2 to resolve collision and not trigger collision again with the passive actor of this
			// collision as the active partner of the next collision with this active ball
			actor1.setCurrentPosition(actor1Position.get(0).doubleValue()-collisionVector.get(0).doubleValue()*3,
					actor1Position.get(1).doubleValue()-collisionVector.get(1).doubleValue()*3);
			actor1.applyCurrentPosition();
			
			// get angle between collision vector coordinate system and global coordinates, since the system is just rotated,
			// the translation can be ignored since only orientation is important
			double angle = Math.acos(collisionVector.get(1).doubleValue());
			
			// get rotated vector 
			List<Double> rotatedVector = Arrays.asList(Math.cos(angle)* collisionVector.get(0).doubleValue()- Math.sin(angle)*collisionVector.get(1).doubleValue(),
					Math.sin(angle)*collisionVector.get(0).doubleValue()+Math.cos(angle)*collisionVector.get(1).doubleValue());

			// differentiate whether it has been turned left or right
			if(!(rotatedVector.get(1) == 1)) {
				angle = -angle;
				rotatedVector = Arrays.asList(Math.cos(angle)* collisionVector.get(0).doubleValue()- Math.sin(angle)*collisionVector.get(1).doubleValue(),
						Math.sin(angle)*collisionVector.get(0).doubleValue()+Math.cos(angle)*collisionVector.get(1).doubleValue());
				}
			
			// with the correct angle now rotate the velocity vector of actor1 to get its values in the new coordinate system
			List<Double> actor1VelocityVectorCollisionCoordinates = Arrays.asList(Math.cos(angle)* actor1Direction.get(0).doubleValue()- Math.sin(angle)*actor1Direction.get(1).doubleValue(),
					Math.sin(angle)*actor1Direction.get(0).doubleValue()+Math.cos(angle)*actor1Direction.get(1).doubleValue());
			
			// with the correct angle now rotate the velocity vector of actor2 to get its values in the new coordinate system
			List<Double> actor2VelocityVectorCollisionCoordinates = Arrays.asList(0.0,0.0);
			if (actor2Velocity != 0) {
				actor2VelocityVectorCollisionCoordinates = Arrays.asList(Math.cos(angle)* actor2Direction.get(0).doubleValue()- Math.sin(angle)*actor2Direction.get(1).doubleValue(),
						Math.sin(angle)*actor2Direction.get(0).doubleValue()+Math.cos(angle)*actor2Direction.get(1).doubleValue());
			}

			// perform calculation of new velocities and directions
			// actor1 velocity in hit direction y-axis
			double actor1NewVelocityY = actor2VelocityVectorCollisionCoordinates.get(1).doubleValue()*actor2Velocity;
			double actor1NewVelocityX = actor1VelocityVectorCollisionCoordinates.get(0).doubleValue()*actor1Velocity;
			
			List<Double> test1 = Arrays.asList(Math.cos(-angle)* actor1NewVelocityX- Math.sin(-angle)*actor1NewVelocityY,
					Math.sin(-angle)*actor1NewVelocityX+Math.cos(-angle)*actor1NewVelocityY);
			
			actor1.setVelocityVector(test1.get(0), test1.get(1));
			actor1.setVelocity(this.norm(actor1NewVelocityX, actor1NewVelocityY)*0.995);
			
			// actor2 velocity in hit direction y-axis
			double actor2NewVelocityY = actor1VelocityVectorCollisionCoordinates.get(1).doubleValue()*actor1Velocity;
			double actor2NewVelocityX = actor2VelocityVectorCollisionCoordinates.get(0).doubleValue()*actor2Velocity;
			
			List<Double> test2 = Arrays.asList(Math.cos(-angle)* actor2NewVelocityX- Math.sin(-angle)*actor2NewVelocityY,
					Math.sin(-angle)*actor2NewVelocityX+Math.cos(-angle)*actor2NewVelocityY);
			
			actor2.setVelocityVector(test2.get(0), test2.get(1));
			actor2.setVelocity(this.norm(actor2NewVelocityX, actor2NewVelocityY)*0.995);
			

		} else if (actor1_ instanceof Ball && actor2_ instanceof SideBorder && actor1_.isActEnabled()) {
			// this block covers the physics when a ball hits the border
			Ball ball = (Ball) actor1_;
			SideBorder border = (SideBorder) actor2_;
			ArrayList<Double> ballVelocityVector = ball.getVelocityVector();
			ArrayList <Double> currentBallPosition = ball.getCurrentPosition();
			// play sound
			this.soundEngine.playSound("borderBounce");
			
			// check for vertical border
			if (border.getNormalVector()[1]==0.0) {
				
				// set new velocity vector which is just a sign change on the x-value
				ball.setVelocityVector(ballVelocityVector.get(0).doubleValue()*-1.0, ballVelocityVector.get(1).doubleValue());
				
				// relocate ball one pixel to prevent triggering collision event again
				ball.setCurrentPosition(border.getPixelLocation().getX()+border.getNormalVector()[0]*11.0,
										currentBallPosition.get(1).doubleValue());
			}else{
				// set new velocity direction
				ball.setVelocityVector(ballVelocityVector.get(0).doubleValue(), ballVelocityVector.get(1).doubleValue()*-1.0);
				
				// relocate ball one pixel to prevent triggering collision event again
				ball.setCurrentPosition(currentBallPosition.get(0).doubleValue(),
										border.getPixelLocation().getY()+border.getNormalVector()[1]*11.0);
				ball.applyCurrentPosition();
			}
			// apply loss due to the rebound to the balls velocity and apply the new position
			ball.setVelocity(ball.getVelocity()*0.85);
			ball.applyCurrentPosition();
			
		}else if (actor1_ instanceof Ball && actor2_ instanceof Hole) {
			Ball ball = (Ball) actor1_;
			Hole hole = (Hole) actor2_;
			// catch case when white ball can be placed with mouse so the collision should not be detected
			if(actor1_ instanceof WhiteBall) {
				WhiteBall whiteBall = (WhiteBall) actor1_;
				if(whiteBall.isPlaceableWithMouse()) {
					return 0;
				}
			}else{
				// increment timer when a ball is falling into the hole and the ball is not white ball
				this.timer.increment(10);
			}
			ball.setActEnabled(false);
			hole.addBallToCollect(ball);

		}
		return 0;
	}
}
