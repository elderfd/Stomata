/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
public class MainMenu extends JPanel {
    MainMenu (MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }
    
    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        playButton = new JButton("Play");
        topScoresLabel = new JLabel("Top Scores");
        
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change layout to show the game window
                mainWindow.showAndStartGame();
            }
        });
        
        topScoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topScoresLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        this.add(Box.createVerticalGlue());
        this.add(playButton);
        this.add(Box.createRigidArea(new Dimension(0,50)));
        this.add(topScoresLabel);
        this.add(Box.createVerticalGlue());
    }
    
    private JButton playButton;
    private JLabel topScoresLabel;
    private MainWindow mainWindow;
}
