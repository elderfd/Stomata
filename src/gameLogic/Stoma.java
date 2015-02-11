/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import utility.Area;
import utility.Location;
import utility.RectangularArea;

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
        if(open) {
            return openSpriteID;
        } else {
            return closedSpriteID;
        }
    }
    
    @Override
    public int getWidth() {
        return width;
    }
     
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public Area getHitBox() {
        RectangularArea hitBox = new RectangularArea(location, width, height);
        
        return hitBox;
    }
    
    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }
    
    // Whether or not the stoma is currently open
    private boolean open = false;

    // Where the stoma is located in space, upper left corner
    private Location location;

    private String openSpriteID = "stomaOpen";
    private String closedSpriteID = "stomaClosed";
    
    // Dimensions of stoma
    private int width = 10;
    private int height = 10;
}