package Billiard;

import java.awt.Point;
import java.util.ArrayList;

import ch.aplu.jgamegrid.Location;

public class WhiteBall extends Ball{
	private Point initial;
	private boolean placeableWithMouse;
	private ArrayList<Boolean> emptySpaceToPlace;   // flag keeps track of the mouse hovering over possible positions where
													// the white ball could not be placed
	private boolean freshPlaced;
	
	public WhiteBall(String spritePath, Point initial) {
		super(spritePath, initial);
		this.initial = initial;
		this.emptySpaceToPlace = new ArrayList<Boolean>();
		this.placeableWithMouse = false;
		this.freshPlaced = false;
	}
	
	@Override
	public void catched() {
		Ball.soundEngine.playSound("ballInHole");
		this.resetPosition();
	}
	
	public void placeWithmouse() {
		this.freshPlaced = true;
		this.setVelocity(0);
		this.setPlaceableWithMouse(true);
		this.hide();
	}
	
	public void mouseShot(Location mouseLocation) {
		double ballX = this.getPixelLocation().getX();
		double ballY = this.getPixelLocation().getY();
		double mouseX = mouseLocation.getX();
		double mouseY = mouseLocation.getY();
		double distanceToMouse = (Math.sqrt(Math.pow(ballX-mouseX, 2)+Math.pow(ballY-mouseY, 2))/3);
		if(distanceToMouse<=20  && distanceToMouse>=0.7) {
			this.setVelocity(distanceToMouse);
		}else if(distanceToMouse >20) {
			this.setVelocity(20);
		}
		this.setVelocityVector(ballX-mouseX, ballY-mouseY);
	}
	
	@Override
	public void resetPosition() {
		this.setCurrentPosition(this.initial.getX(), this.initial.getY());
		this.applyCurrentPosition();
		this.setVelocity(0);
		this.setActEnabled(true);
		this.freshPlaced = true;
		Ball.soundEngine.playSound("ballOnTable");
	}

	public boolean isPlaceableWithMouse() {
		return placeableWithMouse;
	}

	public void setPlaceableWithMouse(boolean placeableWithMouse) {
		if(!this.placeableWithMouse) {
			this.setActEnabled(true);
		}else{
		}
		this.placeableWithMouse = placeableWithMouse;
	}

	public boolean isEmptySpaceToPlace() {
		if(this.emptySpaceToPlace.size()>0) {
			return false;
		}else {
			return true;
		}
	}

	public void addEmptySpaceToPlace() {
		this.emptySpaceToPlace.add(true);
	}
	
	public void removeEmptySpaceToPlace() {
		if (this.emptySpaceToPlace.size()>0) {
		this.emptySpaceToPlace.remove(0);
		}
	}

	public boolean isFreshPlaced() {
		return freshPlaced;
	}

	public void setFreshPlaced(boolean freshPlaced) {
		this.freshPlaced = freshPlaced;
	}
}
