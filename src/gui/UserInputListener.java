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

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON2;
import java.awt.event.MouseListener;
import utility.Location;

/**
 *
 * @author James
 */
public class UserInputListener implements MouseListener, KeyListener {
    public UserInputListener(GamePanel gamePanel, MainWindow mainWindow) {
        leftMouseClickLocation = new Location(0, 0);
        rightMouseClickLocation = new Location(0, 0);
        this.gamePanel = gamePanel;
        this.mainWindow = mainWindow;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case VK_ESCAPE:
                gamePanel.exit();
                break;
        }
    }
    
    public Location getLocationFromMouseEvent(MouseEvent e) {
        return new Location(
            gamePanel.coordTransform().screenWidthToGameWidth(e.getX()),
            gamePanel.coordTransform().screenHeightToGameHeight(e.getY())
        );
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == BUTTON1) {
            leftMouseClicked = true;
            // TODO: Need to check if these match up (not convinced)
            leftMouseClickLocation = getLocationFromMouseEvent(e);
        } else if(e.getButton() == BUTTON2) {
            rightMouseClicked = true;
            rightMouseClickLocation = getLocationFromMouseEvent(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == BUTTON1 && leftMouseClicked) {
            leftMouseClicked = false;
            gamePanel.mouseClickedAt(leftMouseClickLocation);
        } else if(e.getButton() == BUTTON2 && rightMouseClicked) {
            rightMouseClicked = false;
            gamePanel.mouseClickedAt(rightMouseClickLocation);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        leftMouseClicked = false;
        rightMouseClicked = false;
    }
    
    // Not implemented
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    //
    
    
    private boolean leftMouseClicked;
    Location leftMouseClickLocation;
    private boolean rightMouseClicked;
    Location rightMouseClickLocation;
    
    GamePanel gamePanel;
    MainWindow mainWindow;
}
