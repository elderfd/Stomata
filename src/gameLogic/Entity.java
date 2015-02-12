/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import utility.Location;
import utility.RNG;

/**
 *
 * @author James
 */
public abstract class Entity implements DrawableObject {
    public Entity(Location startLocation) {
        this.currentLocation = startLocation;
    }

    public void setLocation(Location newLocation) {
        currentLocation = newLocation;
    }
    
    abstract public boolean shouldDie(RNG rng);
    
    @Override
    public Location getLocation() {
        return currentLocation;
    }
    
    // Updates location to match that at t + 1
    abstract public void updateLocation(RNG rng);
    
    protected Location currentLocation;
}
