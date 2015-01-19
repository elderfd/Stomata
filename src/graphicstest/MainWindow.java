/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicstest;

import javax.swing.JFrame;

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
        
        renderPanel = new GamePanel();
        window.add(renderPanel);
        
        window.setSize(256, 256);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setTitle("Test");
        
        window.setVisible(true);
    }
    
    private JFrame window;
    private GamePanel renderPanel;
    private SpriteManager spriteManager;
}
