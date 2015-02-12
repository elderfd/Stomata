/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import static java.lang.Math.abs;
import utility.Area;
import utility.Location;
import utility.RectangularArea;

/**
 *
 * @author James
 */
public class Pathogen extends Entity {
    public Pathogen(Location startLocation, Stoma target) {
        super(startLocation);
        this.targetStoma = target;
    }

    @Override
    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public String getSpriteID() {
        return spriteID;
    }

    @Override
    public Area getHitBox() {
        RectangularArea hitBox = new RectangularArea(currentLocation, width, height);
        
        return hitBox;
    }

    @Override
    public int getWidth() {
        return width;
    }
     
    @Override
    public int getHeight() {
        return height;
    }
    
    public boolean hasHitTarget() {
        return targetStoma.getHitBox().containsLocation(currentLocation);
    }
    
    @Override
    public void updateLocation() {
        // Move pathogen closer to target

        // Check it hasn't already reached its target
        Location targetCentroid = targetStoma.getHitBox().getCentroid();
        Location currentCentroid = this.getHitBox().getCentroid();
        
        if(!currentCentroid.equals(targetCentroid)) {
            int newX = currentLocation.getX(), newY = currentLocation.getY();
            
            int xDiff = targetCentroid.getX() - currentCentroid.getX();
            int yDiff = targetCentroid.getY() - currentCentroid.getY();
            
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
    private Stoma targetStoma;
    // The number of squares (sorta) that the pathogen can move each time step
    private int speed = 5;
    
    // The dimensions of the pathogen
    private int width = 5;
    private int height = 5;
    
    private String spriteID = "pathogen";
}
