/* 
 * The MIT License
 *
 * Copyright 2015 James.
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
    private int width = 20;
    private int height = 20;
}