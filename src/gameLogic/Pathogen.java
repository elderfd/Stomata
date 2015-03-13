/* 
 * The MIT License
 *
 * Copyright 2015 James Elderfield.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package gameLogic;

import gui.DrawableObject;
import static java.lang.Math.abs;
import timeScaling.RatePerSecond;
import timeScaling.TimeInSeconds;
import utility.Area;
import utility.Location;
import utility.RNG;
import utility.RectangularArea;

/**
 *
 * @author James Elderfield
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
    
    public Stoma getTarget() {
        return targetStoma;
    };
    
    public Location getLocation() {
        return currentLocation;
    }

    public String getSpriteID() {
        if(hasHitTarget()) {
            return infectingSpriteID;
        } else {
            return normalSpriteID;
        }
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
    
    public boolean finishedInfection() {
        return _infected;
    }
    
    public boolean hasHitTarget() {
        return targetStoma.getHitBox().overlapsWith((RectangularArea)this.getHitBox());
    }
    
    public boolean shouldDie(RNG rng, GameState state) {
        boolean dies = false;
        
        // Remove if off edge of screen
        if(currentLocation.getY() < 0
            || currentLocation.getY() > state.getHeightOfArena()
        ) {
            dies = true;
        } 
        
        return dies;
    }
    
    public void updateLocation(RNG rng, GameState state) {
        // Move pathogen closer to target

        // Check it hasn't already reached its target        
        if(!hasHitTarget()) {
            momentum.tweak(rng);
            
            Location targetCentroid = targetStoma.getHitBox().getCentroid();
            Location currentCentroid = this.getHitBox().getCentroid();
            
            int newX = currentLocation.getX(), newY = currentLocation.getY();
            
            int xDiff = targetCentroid.getX() - currentCentroid.getX();
            int yDiff = targetCentroid.getY() - currentCentroid.getY();
            
            // Check if on leaf
            hasLanded = state.isLocationOnLeaf(currentLocation) && yDiff == 0;
            
            int dist = speed;
            
            if(hasLanded) {
                dist = 0;
            }
            
            if(dist > abs(yDiff) && yDiff > 0) {
                dist = abs(yDiff);
            }
            
            newY += dist;
            
            dist = speed;

            // Check if on leaf
            if(hasLanded) {
                 // Tracks sidewards to target (and starts decaying!)                
                if(xDiff < 0) {
                    dist *= -1;
                }
            } else {
                dist *= momentum.getEffect();
            }

            newX += dist;
            
            currentLocation.setX(newX);
            currentLocation.setY(newY);
        } else {
            timeSinceAttemptedInfectionStart.setValue(
                timeSinceAttemptedInfectionStart.value() + TimeInSeconds.timeForOneFrame().value()
            );
            
            if(timeSinceAttemptedInfectionStart.value() >= timeToInfect.value()) {
                _infected = true;
            }
        }
    }
    
    // The target the pathogen is heading for
    private Stoma targetStoma;
    // The number of squares (sorta) that the pathogen can move each time step
    private int speed = 1;
    
    // The dimensions of the pathogen
    private int width = 10;
    private int height = 10;
    
    private String normalSpriteID = "pathogen";
    private String infectingSpriteID = "infectingPathogen";
 
    private boolean _infected = false;
    
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
                strength = 1; 
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
    
    // The amount of time it takes a pathogen to infect a stoma once it has reached it
    static private TimeInSeconds timeToInfect = new TimeInSeconds(0.5);
    
    // How long since this pathogen started an attempted infection
    private TimeInSeconds timeSinceAttemptedInfectionStart = new TimeInSeconds(0);
}
