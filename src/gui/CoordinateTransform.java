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
