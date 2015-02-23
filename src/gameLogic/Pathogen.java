/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import static java.lang.Math.abs;
import timeScaling.RatePerSecond;
import utility.Area;
import utility.Location;
import utility.RNG;
import utility.RectangularArea;

/**
 *
 * @author James
 */
public class Pathogen implements DrawableObject {
    public Pathogen(Location startLocation, Stoma target, RNG rng) {
        this.currentLocation = startLocation;
        this.targetStoma = target;
        momentum = new Momentum(rng);
    }

    public void setLocation(Location newLocation) {
        currentLocation = newLocation;
    }
    
    public Location getLocation() {
        return currentLocation;
    }

    public String getSpriteID() {
        return spriteID;
    }

    public Area getHitBox() {
        RectangularArea hitBox = new RectangularArea(currentLocation, width, height);
        
        return hitBox;
    }

    public int getWidth() {
        return width;
    }
     
    public int getHeight() {
        return height;
    }
    
    public boolean hasHitTarget() {
        return targetStoma.getHitBox().overlapsWith((RectangularArea)this.getHitBox());
    }
    
    public boolean shouldDie(RNG rng) {
        boolean dies = false;
        
        if(hasLanded) {
            dies = rng.bernoulliTrial(decayProbability.toPerFrame().value());
        }
        
        return dies;
    }
    
    public void updateLocation(RNG rng) {
        // Move pathogen closer to target

        // Check it hasn't already reached its target
        Location targetCentroid = targetStoma.getHitBox().getCentroid();
        Location currentCentroid = this.getHitBox().getCentroid();
        
        if(!currentCentroid.equals(targetCentroid)) {
            momentum.tweak(rng);
            
            int newX = currentLocation.getX(), newY = currentLocation.getY();
            
            int xDiff = targetCentroid.getX() - currentCentroid.getX();
            int yDiff = targetCentroid.getY() - currentCentroid.getY();
            
            int dist = speed;
            if(dist > abs(yDiff)) {
                dist = abs(yDiff);
            }
            
            if(yDiff >= 0) {
                newY += dist;
            } else if(yDiff < 0) {
                newY -= dist;
            }
            
            // Don't move if at target height
            if(dist != 0) {
                dist = speed;
                
                dist *= momentum.getEffect();
                
                // Bias towards the stomata
                int maxBias = 2;
                
                if(maxBias > abs(xDiff)) {
                    maxBias = abs(xDiff);
                }
                
                if(rng.bernoulliTrial(0.2)) {
                    if(xDiff < 0) {
                        dist -= maxBias;
                    } else if(xDiff > 0) {
                        dist += maxBias;
                    }
                }  
            }
            
            newX += dist;
            
            currentLocation.setX(newX);
            currentLocation.setY(newY);
        }
    }
    
    // The target the pathogen is heading for
    private Stoma targetStoma;
    // The number of squares (sorta) that the pathogen can move each time step
    private int speed = 1;
    
    // The dimensions of the pathogen
    private int width = 5;
    private int height = 5;
    
    private String spriteID = "pathogen";
 
    // Describes the bias in the movement of the pathogen
    // Should get runs from left to right
    private class Momentum {
        Momentum(RNG rng) {
            direction = rng.bernoulliTrial(0.5);
            strength = 1;
            decay = 0.99;
        }
        
        public void tweak(RNG rng) {
            if(rng.bernoulliTrial(1 - strength)) {
                direction = !direction;
                strength *= 1; 
            } else {
                strength *= decay;
            }
        }
        
        public int getEffect() {
            if(direction) {
                return 1;
            } else {
                return -1;
            }
        }
        
        // True means left, false means right
        private boolean direction;
        
        // Complement of probability of direction change
        private double strength; 
        
        // Decay of strength with each twek
        private double decay;
    };
    private Momentum momentum;
    
    // How many steps the pathogen has existed for
    private int _age;
    
    // Whether or not the pathogen has landed on the surface of the leaf
    private boolean hasLanded = false;
    
    // Decay probability per second
    private RatePerSecond decayProbability = new RatePerSecond(0.4);
    
    protected Location currentLocation;
}
