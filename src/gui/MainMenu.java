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
