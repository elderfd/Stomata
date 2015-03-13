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

import gui.HighScoreManager.ScorePair;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author James Elderfield
 */
public class MainMenu extends JPanel {
    MainMenu (MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }
    
    public void saveState() {
        highScoreManager.saveScoresToFile(highScoreFileName);
    }
    
    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel titleLabel = new JPanel() {
            JPanel loadImage() {
                try {
                    image = ImageIO.read(new File("images/title.png"));
                } catch(IOException e) {
                    // Shouldn't happen meh, hard crash
                    System.exit(1);
                }
                
                return this;
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                Graphics2D g2d = (Graphics2D) g;
                
                g2d.setColor(Color.black);
                g2d.drawRect(0, 0, this.getWidth(), this.getHeight());
                g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);         
            }
            
            Image image;
        }.loadImage();
        
        playButton = new JButton("Play");
        topScoresLabel = new JLabel("Top Scores");
        
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        playButton.setBackground(Color.black);
        playButton.setFocusPainted(false);
        playButton.setForeground(cambridgeBlue);
        playButton.setFont(playButton.getFont().deriveFont(30f));
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change layout to show the game window
                mainWindow.showAndStartGame();
            }
        });
        
        topScoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topScoresLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        topScoresLabel.setFont(topScoresLabel.getFont().deriveFont(30f));
        topScoresLabel.setForeground(cambridgeBlue);
        
        topScoresArea.setBackground(Color.black);
        
        this.add(Box.createVerticalGlue());
        this.add(titleLabel);
        this.add(Box.createRigidArea(new Dimension(0,25)));
        this.add(playButton);
        this.add(Box.createRigidArea(new Dimension(0,25)));
        this.add(topScoresLabel);
        this.add(topScoresArea);
        
        highScoreManager.updateFromFile(highScoreFileName);
        populateHighScores();
        
        titleLabel.setPreferredSize(new Dimension(1000, 406));
        this.setBackground(Color.black);
    }
    
    public void populateHighScores() {
        int numberExpected = 10;
        
        List<ScorePair> highScores = highScoreManager.getTopNScores(numberExpected);
        
        topScoresArea.removeAll();
        topScoresArea.setLayout(new GridBagLayout());
        
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridx = 0;
        nameConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameConstraints.weightx = 0;
        nameConstraints.ipadx = 25;
        nameConstraints.anchor = GridBagConstraints.NORTH;
        nameConstraints.weighty = 1;
        
        GridBagConstraints scoreConstraints = new GridBagConstraints();
        scoreConstraints.gridx = 1;
        scoreConstraints.fill = GridBagConstraints.HORIZONTAL;
        scoreConstraints.weightx = 0;
        scoreConstraints.ipadx = 25;
        scoreConstraints.anchor = GridBagConstraints.NORTH;
        scoreConstraints.weighty = 1;
        
        int rowCounter = 0;
        
        Font fontToUse = playButton.getFont().deriveFont(20f);
        
        for(ScorePair scorePair : highScores) {
            nameConstraints.gridy = scoreConstraints.gridy = rowCounter;
            
            JLabel nameLabel = new JLabel(scorePair.playerName);
            nameLabel.setVerticalAlignment(SwingConstants.TOP);
            nameLabel.setFont(fontToUse);
            nameLabel.setForeground(cambridgeBlue);
            
            JLabel scoreLabel = new JLabel(String.format(
                "%.2f",
                scorePair.score
            ));
            scoreLabel.setVerticalAlignment(SwingConstants.TOP);
            scoreLabel.setFont(fontToUse);
            scoreLabel.setForeground(cambridgeBlue);
            
            topScoresArea.add(nameLabel, nameConstraints);
            topScoresArea.add(scoreLabel, scoreConstraints);
            
            rowCounter++;
        }
        
        // Fill the gaps
        String dummyName = "";
        String dummyScore = "";
        
        while(rowCounter < numberExpected) {
            nameConstraints.gridy = scoreConstraints.gridy = rowCounter;
            
            JLabel nameLabel = new JLabel(dummyName);
            nameLabel.setVerticalAlignment(SwingConstants.TOP);
            
            JLabel scoreLabel = new JLabel(dummyScore);
            scoreLabel.setVerticalAlignment(SwingConstants.TOP);
            
            topScoresArea.add(nameLabel, nameConstraints);
            topScoresArea.add(scoreLabel, scoreConstraints);
            
            rowCounter++;
        }
        
        this.add(topScoresArea);
        this.revalidate();
        this.repaint();
    }
    
    public void askToRecordScore(double score) {
        String playerName = JOptionPane.showInputDialog(
            this,
            "Enter name to record score",
            "Player name",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if(!playerName.isEmpty()) {
            highScoreManager.addScore(highScoreManager.new ScorePair(playerName, score));
            populateHighScores();
        }
    }
 
    Color cambridgeBlue = new Color(166, 211, 200);
    
    private JButton playButton;
    private JLabel topScoresLabel;
    private JPanel topScoresArea = new JPanel();
    private MainWindow mainWindow;
    static private String highScoreFileName = "highScores.json";
    private HighScoreManager highScoreManager = new HighScoreManager(highScoreFileName);
}
