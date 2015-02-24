/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gameLogic.GameState;
import gameLogic.GameWorker;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;
import utility.Location;

/**
 *
 * @author James
 */
public class GamePanel extends JPanel {
    public GamePanel(GameState state, MainWindow mainWindow) {
        spriteManager = new SpriteManager();
        everythingToDraw = new ArrayList<>();
        inputListener = new UserInputListener(this, mainWindow);
        addMouseListener(inputListener);
        addKeyListener(inputListener);
        _coordTransform = new CoordinateTransform(this);
        
        this.mainWindow = mainWindow;
        this.state = state;
        update();
    }
    
    public void update () {
        everythingToDraw = state.getAllDrawableObjects();
        repaint();
    }
    
    public void exit() {
        if(workThread != null) {
            workThread.cancel();
        }
        mainWindow.showMainMenu();
    }
    
    public int getArenaHeight() {
        return state.getHeightOfArena();
    }
    
    public int getArenaWidth() {
        return state.getWidthOfArena();
    }
 
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        // First empty the screen
        g2d.clearRect(0, 0, getWidth(), getHeight());
        
        // Draw the background
        g2d.drawImage(spriteManager.getSpriteImage("background"),
            0,
            0,
            this.getWidth(),
            this.getHeight(),
            this
        );
        
        // Then draw out all of the objects
        for(DrawableObject object : everythingToDraw) {
            Location screenLocation = coordTransform().gameWorldToScreen(object.getLocation());
            
            g2d.drawImage(
                spriteManager.getSpriteImage(object.getSpriteID()),
                screenLocation.getX(),
                screenLocation.getY(),
                coordTransform().gameWidthToScreenWidth(object.getWidth()),
                coordTransform().gameHeightToScreenHeight(object.getHeight()),
                this
            );
        }
        
        // Finally draw on the number of points
        int pointsLabelX = 30;
        int pointsLabelY = 30;
        
        String pointsLabel = "Points: " + String.format(
            "%.2f",
            state.pointsManager().points()
        );
        g2d.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        
        g2d.drawString(pointsLabel, pointsLabelX, pointsLabelY);
        
        // Game is finished then present a message saying so
        if(state.finished) {
            String finishedLabel = "Game finished. Press ESC to continue.";
            int stringLength = (int)g2d.getFontMetrics().getStringBounds(finishedLabel, g2d).getWidth();
            g2d.drawString(finishedLabel, (this.getWidth() - stringLength)/ 2, this.getHeight() / 2);
        }
    }
    
    public CoordinateTransform coordTransform() {
        return _coordTransform;
    }
    
    // Sets the game running
    public void run() {  
        workThread = new GameWorker(state, this, mainWindow);
        
        workThread.execute();
    }
    
    public void resetGame() {
        state.reset();
    }
    
    public void mouseClickedAt(Location clickLocation) {
        if(state.toggleAnyStomataAtLocation(clickLocation)) update();
    }
    
    private SpriteManager spriteManager;
    private ArrayList<DrawableObject> everythingToDraw;
    private GameState state;
    private UserInputListener inputListener;
    private MainWindow mainWindow;
    private CoordinateTransform _coordTransform;
    private GameWorker workThread = null;
}
