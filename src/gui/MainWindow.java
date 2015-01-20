/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gameLogic.GameState;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author James
 */
public class MainWindow{
    public MainWindow() {
        initGUI();
           
        renderPanel.run();
    }
    
    private void initGUI() {
        window = new JFrame();
        
        // TODO: This will live elsewhere later, just here for test
        GameState state = new GameState();
        
        renderPanel = new GamePanel(state);
        window.add(renderPanel);
        
        window.setSize(1000, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setTitle("Test");
        
        window.setVisible(true);
    }
    
    private JFrame window;
    private GamePanel renderPanel;
    private SpriteManager spriteManager;
}
