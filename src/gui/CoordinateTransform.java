/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        int screenPixelsPerHeight = panel.getHeight() / panel.getArenaHeight();
        
        return gameHeight * screenPixelsPerHeight;
    }
    
    public int gameWidthToScreenWidth(int gameWidth) {
        int screenPixelsPerWidth = panel.getWidth() / panel.getArenaWidth();
        
        return gameWidth * screenPixelsPerWidth;
    }
    
    public int screenHeightToGameHeight(int screenHeight) {
        int screenPixelsPerHeight = panel.getHeight() / panel.getArenaHeight();
        
        return screenHeight / screenPixelsPerHeight;
    }
    
    public int screenWidthToGameWidth(int screenWidth) {
        int screenPixelsPerWidth = panel.getWidth() / panel.getArenaWidth();
        
        return screenWidth / screenPixelsPerWidth;
    }
    
    // Needs to be attached to a panel to allow proper transformation
    private GamePanel panel;
}
