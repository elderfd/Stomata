/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gameLogic.GameState;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
public class GamePanel extends JPanel {
    public GamePanel() {
        spriteManager = new SpriteManager();
        everythingToDraw = new ArrayList<DrawableObject>();
        state = new GameState();
        update(state);
    }
    
    public void update (GameState state) {
        everythingToDraw = state.getAllDrawableObjects();
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        // First empty the screen
        g2d.clearRect(0, 0, getWidth(), getHeight());
        
        // Need to work out the appropriate scaling for the images
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        int arenaWidth = state.getWidthOfArena();
        int arenaHeight = state.getHeightOfArena();
        
        int widthOfCell = screenWidth / arenaWidth;
        int heightOfCell = screenHeight / arenaHeight;
        
        // Draw the background
        g2d.drawImage(spriteManager.getSpriteImage("background"),
            0,
            0,
            screenWidth,
            screenHeight,
            this
        );
        
        // Then draw out all of the objects
        for(DrawableObject object : everythingToDraw) {
            g2d.drawImage(spriteManager.getSpriteImage(object.getSpriteID()),
                object.getLocation().getX() * widthOfCell,
                object.getLocation().getY() * heightOfCell,
                widthOfCell,
                heightOfCell,
                this
            );
        }
    }
    
    private SpriteManager spriteManager;
    private ArrayList<DrawableObject> everythingToDraw;
    
    // TODO: This will live elsewhere later, just here for test
    GameState state;
}
