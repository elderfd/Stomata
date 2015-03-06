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

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import gameLogic.GameState;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author James Elderfield
 */
public class MainWindow{
    public MainWindow() {
        initGUI();
    }
    
    private void initGUI() {
        window = new JFrame();
        
        // TODO: This will live elsewhere later, just here for test
        GameState state = new GameState();
        
        windowPanel = new JPanel(new CardLayout());
        
        window.add(windowPanel);
        
        window.setSize(1000, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setTitle("Test");
        
        renderPanel = new GamePanel(state, this);
        _mainMenu = new MainMenu(this);
        
        windowPanel.add(_mainMenu, MAIN_MENU_IDENTIFIER);
        windowPanel.add(renderPanel, GAME_PANEL_IDENTIFIER);
        
        window.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    _mainMenu.saveState();
                }
            }
        );
        
        window.setVisible(true);
    }
    
    public void showAndStartGame() {
        renderPanel.resetGame();
        
        CardLayout layout = (CardLayout)windowPanel.getLayout();
        layout.show(windowPanel, GAME_PANEL_IDENTIFIER);

        renderPanel.update();
        renderPanel.requestFocus();
        renderPanel.run();
    }
    
    public void showMainMenu() {
        CardLayout layout = (CardLayout)windowPanel.getLayout();
        layout.show(windowPanel, MAIN_MENU_IDENTIFIER);
    }
    
    public MainMenu mainMenu() {
        return _mainMenu;
    }
    
    private JFrame window;
    private GamePanel renderPanel;
    private MainMenu _mainMenu;
    private JPanel windowPanel;
    
    private final String GAME_PANEL_IDENTIFIER = "GAMEBOARD";
    private final String MAIN_MENU_IDENTIFIER = "MAINMENU";
}
