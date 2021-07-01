package Billiard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

public class StandardGame extends GameGrid implements GGMouseListener, GGMouseTouchListener, WindowInterface{
	private WhiteBall whiteBall;
	private PhysixEngine physixEngine;
	private Timer timer;
	private boolean noShot;  // variable to control cancel a shot with right moouse button
	private SoundEngine soundEngine;
	private TextActor[] endScreenText = new TextActor[2];
	private int bgDrawDevider = 5;
	private int bgDrawCounter = 0;
	
	
	public StandardGame(WindowController controller, SoundEngine soundEngine) {
		super(160*5, 88*5, 1, null, "assets/PoolTable.png", false);
		this.noShot = false;
		this.soundEngine = soundEngine;
		this.addKeyListener(controller);
		this.setName("game");
		this.addFinishScreen();
		
	}
	
	@Override
	public void initialize() {
		this.setTitle("Billiard-Mode");
		this.setSimulationPeriod(20);
		
		// create timer and place on the field
		this.timer = new Timer(120,new Location(140,13),this.getSimulationPeriod(),this);
		
		// initialize physix engine for collision treatment
		PhysixEngine physicsEngine = new PhysixEngine(this.timer, this.soundEngine);
		this.physixEngine = physicsEngine;
		
		// add sound engine to ball class, very ugly but works
		Ball.addSoundEngine(this.soundEngine);
		
		// this is the Billiard setup of balls which is created here
		this.addToGridAtInitial(new Ball("assets/kugel_11.gif", new Point(this.getBgImagePosX()+218,this.getBgImagePosY()+218)));
		this.addToGridAtInitial(new Ball("assets/kugel_1.gif", new Point(this.getBgImagePosX()+200,this.getBgImagePosY()+207)));
		this.addToGridAtInitial(new Ball("assets/kugel_3.gif", new Point(this.getBgImagePosX()+200,this.getBgImagePosY()+228)));
		this.addToGridAtInitial(new Ball("assets/kugel_8.gif", new Point(this.getBgImagePosX()+182,this.getBgImagePosY()+218)));
		this.addToGridAtInitial(new Ball("assets/kugel_13.gif", new Point(this.getBgImagePosX()+182,this.getBgImagePosY()+239)));
		this.addToGridAtInitial(new Ball("assets/kugel_14.gif", new Point(this.getBgImagePosX()+181,this.getBgImagePosY()+198)));
		this.addToGridAtInitial(new Ball("assets/kugel_7.gif", new Point(this.getBgImagePosX()+162,this.getBgImagePosY()+187)));
		this.addToGridAtInitial(new Ball("assets/kugel_2.gif", new Point(this.getBgImagePosX()+161,this.getBgImagePosY()+228)));
		this.addToGridAtInitial(new Ball("assets/kugel_0.gif", new Point(this.getBgImagePosX()+162,this.getBgImagePosY()+208)));
		this.addToGridAtInitial(new Ball("assets/kugel_9.gif", new Point(this.getBgImagePosX()+163,this.getBgImagePosY()+249)));
		this.addToGridAtInitial(new Ball("assets/kugel_5.gif", new Point(this.getBgImagePosX()+145,this.getBgImagePosY()+259)));
		this.addToGridAtInitial(new Ball("assets/kugel_6.gif", new Point(this.getBgImagePosX()+144,this.getBgImagePosY()+239)));
		this.addToGridAtInitial(new Ball("assets/kugel_10.gif", new Point(this.getBgImagePosX()+143,this.getBgImagePosY()+219)));
		this.addToGridAtInitial(new Ball("assets/kugel_4.gif", new Point(this.getBgImagePosX()+143,this.getBgImagePosY()+198)));
		this.addToGridAtInitial(new Ball("assets/kugel_12.gif", new Point(this.getBgImagePosX()+144,this.getBgImagePosY()+177)));
		
		// place white ball
		this.whiteBall = new WhiteBall("assets/kugel_weiss.gif", new Point(this.getBgImagePosX()+580,this.getBgImagePosY()+218));
		this.addToGridAtInitial(this.whiteBall);
		this.whiteBall.setVelocityVector(10,-3.4);
		
		// place actors for holes
		Hole test = new Hole(new Location(35,35),13);
		this.addHole(test); // hole left top
		this.addHole(new Hole(new Location(763,35),30)); // hole right top
		this.addHole(new Hole(new Location(763,400),13)); // hole right down
		this.addHole(new Hole(new Location(35,400),13)); // hole left down
		this.addHole(new Hole(new Location(398,22),8)); // hole middle top
		this.addHole(new Hole(new Location(398,412),8)); // hole middle down
		
		
		// place actors for sidebars
		// define normal vectors to pass to sideborder which is important for ball deflection
		double[] leftNormalVector = {1.0, 0.0};
		double[] rightNormalVector = {-1.0, 0.0};
		double[] topNormalVector = {0.0, 1.0};
		double[] bottomNormalVector = {0.0, -1.0};
		
		// place sideBorder left this needs for some reason a rotation
		SideBorder leftBorder = new SideBorder(new Location(38,218),true, leftNormalVector);
		this.addSideBorder(leftBorder);
		leftBorder.rotate(leftBorder.getPixelLocation(), 90);
		// place sideBorder right this needs for some reason a rotation
		SideBorder rightBorder = new SideBorder(new Location(760,218),true, rightNormalVector);
		this.addSideBorder(rightBorder);
		rightBorder.rotate(rightBorder.getPixelLocation(), 90);
		this.addSideBorder(new SideBorder(new Location(222,37), topNormalVector)); // sidebar left top
		this.addSideBorder(new SideBorder(new Location(575,37), topNormalVector)); // sidebar right top
		this.addSideBorder(new SideBorder(new Location(222,397),bottomNormalVector)); // sidebar left bottom
		this.addSideBorder(new SideBorder(new Location(575,397),bottomNormalVector)); // sidebar right bottom
		
		// add collision candidates for every ball
		// ugly code which needs some optimization if there is time left
		for (Actor ball:this.getActors()) {
			if(ball instanceof Ball) {
				ball.addActorCollisionListener(physicsEngine);
				ball.setCollisionCircle(new Point(0,0),10);
				for(Actor ballToAdd:this.getActors()) {
					if(!ballToAdd.equals(ball)) {
						ball.addCollisionActor(ballToAdd);
					}
				}
			}
		}

		// add listener for pool stick function/ placing white ball function
		this.addMouseListener(this, GGMouse.move|GGMouse.lClick|GGMouse.lDrag|GGMouse.lRelease|GGMouse.rPress|GGMouse.lPress);
	}
	
	public void useTimer(int time, Location location, int loopTime) {
		Timer timer = new Timer(time,location, loopTime, this);
		timer.show();
	}
	
	public boolean timeOver() {
		return this.timer.timeOver();
	}
	
	private void addToGridAtInitial(Ball actor) {
		this.addActor(actor, new Location(0,0));
		actor.setPixelLocation(actor.getInitialPoint());
		double randomAngle = (double) ThreadLocalRandom.current().nextInt(0, 3 + 1)*90;
		actor.rotate(actor.getInitialPoint(), randomAngle);
	}
	
	
	private void addSideBorder(SideBorder border) {
		border.addActorCollisionListener(this.physixEngine);
		border.addMouseTouchListener(this, GGMouse.enter | GGMouse.leave);
		this.addActor(border, border.getStartPoint());
	}
	
	private void addHole(Hole hole) {
		hole.addActorCollisionListener(this.physixEngine);
		hole.addMouseTouchListener(this, GGMouse.enter | GGMouse.leave);
		this.addActor(hole, hole.getStartPoint());
		hole.setCollisionCircle(0,new Point(0,0), 15);
	}

	@Override
	public boolean mouseEvent(GGMouse mouse) {
		// TODO Auto-generated method stub
		Location mouseLocation = getMouseLocation();
		if(mouse.getEvent() == GGMouse.lClick) {
			if(this.whiteBall.getVelocity() == 0 && !this.whiteBall.isPlaceableWithMouse()) {
				this.whiteBall.setFreshPlaced(false);  // when the ball could be shot, it is in a position where it is not freshly placed anymore
			} else if(this.whiteBall.isPlaceableWithMouse() && this.whiteBall.isEmptySpaceToPlace()) {
				if(this.getActorsAt(mouseLocation).size()<2) {
					this.whiteBall.setCurrentPosition(mouseLocation.getX(), mouseLocation.getY());
					this.whiteBall.applyCurrentPosition();
					this.whiteBall.setPlaceableWithMouse(false);
				}
			}
			this.refresh();
			return true;
		}else if(mouse.getEvent() == GGMouse.move) {
			if(this.whiteBall.isPlaceableWithMouse()) {
				if (!this.whiteBall.isVisible()) {
					this.whiteBall.show();
				}
				this.whiteBall.setLocation(mouseLocation);
				this.refresh();
			}
			return true;
		} else if(mouse.getEvent() == GGMouse.lDrag) {
			if(this.whiteBall.getVelocity()==0 && !this.whiteBall.isPlaceableWithMouse() && !this.noShot) {
				this.drawPokeindicatorline(mouseLocation);
			}
		}else if(mouse.getEvent() == GGMouse.lPress) {
			this.noShot = false;
		} else if(mouse.getEvent() == GGMouse.lRelease) {
			if(this.whiteBall.getVelocity() == 0 && !this.whiteBall.isPlaceableWithMouse() && !this.noShot) {
				this.whiteBall.setFreshPlaced(false);  // when the ball could be shot, it is in a position where it is not freshly placed anymore
				this.whiteBall.mouseShot(mouseLocation);
				this.clearPokeIndicatorLine(this.getBg());
			} else if(this.whiteBall.isPlaceableWithMouse() && this.whiteBall.isEmptySpaceToPlace()) {
				if(this.getActorsAt(mouseLocation).size()<2) {
					this.whiteBall.setCurrentPosition(mouseLocation.getX(), mouseLocation.getY());
					this.whiteBall.applyCurrentPosition();
					this.whiteBall.setPlaceableWithMouse(false);
				}
			}
			this.refresh();
			this.noShot = false;
			return true;
		}else if(mouse.getEvent() == GGMouse.rPress) {
			this.clearPokeIndicatorLine(this.getBg());
			this.noShot = true;
		}
		return false;
	}
	
	private void drawPokeindicatorline(Location mouseLocation) {
		// function to draw line at white ball for better aiming
		this.bgDrawCounter = this.bgDrawCounter+1;
		if(this.bgDrawCounter == this.bgDrawDevider) {
			this.bgDrawCounter = 0;
			GGBackground bg= this.getBg();
			this.clearPokeIndicatorLine(bg);
			// only draw line when mouse is in field an white ball is not rolling
			if (getMousePosition() != null && this.whiteBall.getVelocity() == 0.0) {
				Point startPoint = this.whiteBall.getPixelLocation();
				double[] whiteBallToMouse = {this.whiteBall.getPixelLocation().getX()-mouseLocation.getX(),this.whiteBall.getY()-mouseLocation.getY()};
				double[] normedWhiteBallToMouse = {(this.whiteBall.getPixelLocation().getX()-mouseLocation.getX())/norm(whiteBallToMouse),
												   (this.whiteBall.getPixelLocation().getY()-mouseLocation.getY())/norm(whiteBallToMouse)};
				double endPointX = 0.0;
				double endPointY = 0.0;
				// set line which shows ball direction accordingly to the forde of the stick
				if(norm(whiteBallToMouse)/3<20) {
					endPointX = this.whiteBall.getPixelLocation().getX()+(this.whiteBall.getPixelLocation().getX()-mouseLocation.getX())*norm(whiteBallToMouse)/20;
					endPointY = this.whiteBall.getPixelLocation().getY()+(this.whiteBall.getPixelLocation().getY()-mouseLocation.getY())*norm(whiteBallToMouse)/20;
				}else{
					endPointX = this.whiteBall.getPixelLocation().getX()+normedWhiteBallToMouse[0]*198;
					endPointY = this.whiteBall.getPixelLocation().getY()+normedWhiteBallToMouse[1]*198;
					
				}
				bg.setLineWidth(3);
				bg.drawLine(startPoint, new Point((int)endPointX,(int)endPointY));
			}
		}
	}
	
	private void clearPokeIndicatorLine(GGBackground bg) {
		BufferedImage bgImage = bg.getBackgroundImage();
		bg.clear();
		bg.drawImage(bgImage);
	}

	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		// TODO Auto-generated method stub
		if(mouse.getEvent() == GGMouse.enter) {
			this.whiteBall.addEmptySpaceToPlace();
		}else if(mouse.getEvent() == GGMouse.leave) {
			this.whiteBall.removeEmptySpaceToPlace();
		}
	}
	
	public void act() {
		// act function of timer to count down and pause game if time is over
		if(this.timer.act()) {
			this.toggleFinishScreen();
			this.setActEnabled(false);
			this.doPause();
		}
		if(this.numberVisibleBall()<2) {
			this.whiteBall.resetPosition();
			this.resetBalls();
		}
	}
	
	public void resetBalls() {
		// when all balls are in the holes reset balls on table
		this.whiteBall.setFreshPlaced(true);
		for(Actor ball_:this.getActors(Ball.class)) {
			Ball ball = (Ball) ball_;
			if(!(ball instanceof WhiteBall)) {
				ball.resetPosition();
			}
		}
	}
	
	public void addFinishScreen() {
		// actors for finish screen
		TextActor timeUp = new TextActor("TIME OVER", Color.BLUE,new Color(0, 0, 0, 0), new Font(Font.SANS_SERIF, Font.BOLD, 80));
		TextActor timeUpAdditional = new TextActor("[ESC] - End Game", Color.BLUE,new Color(0, 0, 0, 0), new Font(Font.SANS_SERIF, Font.BOLD, 30));
		this.endScreenText[0] = timeUp;
		this.endScreenText[1] = timeUpAdditional;
		this.addActor(this.endScreenText[0], new Location(180, 200));
		this.addActor(this.endScreenText[1], new Location(260, 300));
		timeUp.setOnTop();
		timeUpAdditional.setOnTop();
		timeUp.hide();
		timeUpAdditional.hide();
	}
	
	public void toggleFinishScreen() {
		for(TextActor text : this.endScreenText) {
			if(text.isVisible() == true) {
				text.hide();
			}else{
				text.show();
			}
		}
		this.refresh();
	}
	
	private int numberVisibleBall(){
		ArrayList<Actor> actors = this.getActors(Ball.class);
		int visibleBalls = 0;
		for(Actor ball:actors) {
			if(ball.isVisible()) {
				++visibleBalls;
			}
		}
		return visibleBalls;
	}
	
	public void restart() {
		this.resetBalls();
		if(this.endScreenText[0].isVisible()) {
			this.toggleFinishScreen();
		}
		this.whiteBall.resetPosition();
		this.timer.reset();
		this.setActEnabled(true);
	}
	
	private double norm(double[] vector) {
		// calc euclid norm of an n-dimensional vector
		double result = 0;
		for(int i = 0;i<vector.length; i++) {
			result = result+Math.pow(vector[i], 2);
		}
		return Math.sqrt(result);
	}

	
	@Override
	public void reset() {
		this.restart();
		this.doRun();
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
