/* 
 * The MIT License
 *
 * Copyright 2015 James Elderfield.
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
import utility.RectangularArea;

/**
 *
 * @author James Elderfield
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
        calculateLightingColor();
        repaint();
    }
    
    public void exit() {
        if(workThread != null) {
            workThread.cancel();
        }
        mainWindow.showMainMenu();
        
        if(state.finished) {
            mainWindow.mainMenu().askToRecordScore(state.pointsManager().points());
        }
    }
    
    public int getArenaHeight() {
        return state.getHeightOfArena();
    }
    
    public int getArenaWidth() {
        return state.getWidthOfArena();
    }
    
    public void calculateLightingColor() {
        double brightnessFactor = state.lightManager().getBrightnessFactor();
        
        // Produce a properly scaled lighting color
        int red = 0, green = 0, blue = 0;
        
        int brightestAlpha = 0;
        int darkestAlpha = 200;
        int alphaDiff = darkestAlpha - brightestAlpha;
        
        int alpha = (int) ((1 - brightnessFactor) * alphaDiff + brightestAlpha);
        
        if(brightnessFactor > 0) {
            int i = 0;
        }
        
        lightingColor = new Color(red, green, blue, alpha);
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
        
        // Draw on a sun
        Location sunLocation = state.lightManager().getSunLocation(this);
        
        if(sunLocation != null) {
            g2d.setColor(Color.yellow);
            g2d.fillOval(sunLocation.getX() - 50, sunLocation.getY() - 50, 100, 100);
        }
        
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
            
            // Draw hitboxes for debug
            RectangularArea area = (RectangularArea)object.getHitBox();
            g2d.setColor(Color.black);
            g2d.drawRect(
                    coordTransform().gameWidthToScreenWidth(area.getUpperLeft().getX()),
                    coordTransform().gameHeightToScreenHeight(area.getUpperLeft().getY()),
                    coordTransform().gameWidthToScreenWidth(area.width()),
                    coordTransform().gameHeightToScreenHeight(area.height())
            );
        }
        
        // Add some effects
        for(VisualEffect effect : state.getVisualEffects()) {
            Location screenLocation = coordTransform().gameWorldToScreen(effect.getLocation());
            
            g2d.drawImage(
                spriteManager.getSpriteImage(effect.getSpriteID()),
                screenLocation.getX(),
                screenLocation.getY(),
                coordTransform().gameWidthToScreenWidth(effect.getWidth()),
                coordTransform().gameHeightToScreenHeight(effect.getHeight()),
                this
            );
        }
        
        state.updateEffects();
        
        // Add lighting effects
        g2d.setColor(lightingColor);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        // Finally draw on some useful info
        int labelX = 30;
        int ySkip = 30;
        int labelY = ySkip;
        
        String pointsLabel = "Points: " + String.format(
            "%.2f",
            state.pointsManager().points()
        );
        g2d.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        
        g2d.drawString(pointsLabel, labelX, labelY);
        
        labelY += ySkip;
        
        String timeLabel = "Time: " + String.format(
            "%02d:00",
            (int)state.timeInDay()
        );
        
        g2d.drawString(timeLabel, labelX, labelY);
        
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
    private Color lightingColor;
}
