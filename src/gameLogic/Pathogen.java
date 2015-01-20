/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import static java.lang.Math.abs;
import utility.Location;

/**
 *
 * @author James
 */
public class Pathogen extends Entity {
    public Pathogen(Location startLocation, Stoma target) {
        super(startLocation);
        this.targetLocation = target.getLocation();
    }

    @Override
    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public String getSpriteID() {
        return spriteID;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }
    
    public void updateLocation() {
        // Move pathogen one square closer to target

        // TODO: Make moving better

        // Check it hasn't already reached its target
        if(!targetLocation.equals(currentLocation)) {
            int newX = currentLocation.getX(), newY = currentLocation.getY();

            int xDiff = targetLocation.getX() - currentLocation.getX();
            int yDiff = targetLocation.getY() - currentLocation.getY();
            
            // Avoid shooting over the target
            int dist = speed;
            if(dist > abs(xDiff)) {
                dist = abs(xDiff);
            }
            
            if(xDiff > 0) {
                newX += dist;
            } else if(xDiff < 0) {
                newX -= dist;
            }

            dist = speed;
            if(dist > abs(yDiff)) {
                dist = abs(yDiff);
            }
            
            if(yDiff > 0) {
                newY += dist;
            } else if(yDiff < 0) {
                newY -= dist;
            }

            currentLocation.setX(newX);
            currentLocation.setY(newY);
        }
    }
    
    // The target the pathogen is heading for
    private Location targetLocation;
    // The number of squares (sorta) that the pathogen can move each time step
    private int speed = 1;
    
    private String spriteID = "pathogen";
}
