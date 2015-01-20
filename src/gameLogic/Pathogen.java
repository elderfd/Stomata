/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

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

    // The target the pathogen is heading for
    private Location targetLocation;
    private String spriteID = "pathogen"; // TODO: Bring in an image for this
}
