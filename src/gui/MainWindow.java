/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gameLogic.GameState;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author James
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
        mainMenu = new MainMenu(this);
        
        windowPanel.add(mainMenu, MAIN_MENU_IDENTIFIER);
        windowPanel.add(renderPanel, GAME_PANEL_IDENTIFIER);
        
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
    
    private JFrame window;
    private GamePanel renderPanel;
    private MainMenu mainMenu;
    private JPanel windowPanel;
    
    private final String GAME_PANEL_IDENTIFIER = "GAMEBOARD";
    private final String MAIN_MENU_IDENTIFIER = "MAINMENU";
}
