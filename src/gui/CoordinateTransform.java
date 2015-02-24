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
package gui;

import utility.Location;

/**
 *
 * @author James
 */
public class CoordinateTransform {
    public CoordinateTransform(GamePanel panel) {
        this.panel = panel;
    }
    
    public Location gameWorldToScreen(Location worldCoordinate) {
        return new Location(
            gameWidthToScreenWidth(worldCoordinate.getX()),
            gameHeightToScreenHeight(worldCoordinate.getY())
        );
    }
    
    public Location screenToGameWorld(Location screenCoordinate) {  
        return new Location(
            screenWidthToGameWidth(screenCoordinate.getX()),
            screenHeightToGameHeight(screenCoordinate.getY())
        );
    }
    
    public int gameHeightToScreenHeight(int gameHeight) {
        return panel.getHeight() * gameHeight / panel.getArenaHeight();
    }
    
    public int gameWidthToScreenWidth(int gameWidth) {
        return panel.getWidth() * gameWidth / panel.getArenaWidth();
    }
    
    public int screenHeightToGameHeight(int screenHeight) {
        return panel.getArenaHeight() * screenHeight / panel.getHeight();
    }
    
    public int screenWidthToGameWidth(int screenWidth) {
        return panel.getArenaWidth() * screenWidth / panel.getWidth();
    }
    
    // Needs to be attached to a panel to allow proper transformation
    private GamePanel panel;
}
