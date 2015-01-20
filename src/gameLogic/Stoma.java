/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import utility.Location;

/**
 *
 * @author James
 */
public class Stoma implements DrawableObject {
    public Stoma(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getSpriteID() {
        return spriteID;
    }

    // Whether or not the stoma is currently open
    private boolean open = false;

    // Where the stoma is located in space
    private Location location;

    private String spriteID = "stoma";
}