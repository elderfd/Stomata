/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.GamePanel;
import gui.MainWindow;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author James
 */
public class GameWorker extends SwingWorker<Void, Void> {
    public GameWorker(GameState state, GamePanel gamePanel, MainWindow mainWindow) {
        this.state = state;
        this.gamePanel = gamePanel;
        this.mainWindow = mainWindow;
    }
    
    public void cancel() {
        cancelRequested = true;
    }
    
    @Override
    public Void doInBackground() {
        boolean keepRunning = true;
        long minGameStepIntervalInMillisecs = (long) (MIN_GAME_STEP_INTERVAL_IN_SECS * 1000);
        long timeOfLastGameStep = System.currentTimeMillis();
        int maxGameSteps = (int) (MAX_GAME_TIME_IN_SECS / MIN_GAME_STEP_INTERVAL_IN_SECS);
        int gameStepCounter = 0;
        
        while(keepRunning) {
            if(cancelRequested) {
                return null;
            }
            
            // Check if we need to move the state on and redraw things
            if(System.currentTimeMillis() >= timeOfLastGameStep + minGameStepIntervalInMillisecs) {
                state.updateGameState();
                timeOfLastGameStep = System.currentTimeMillis();
                gameStepCounter ++;
                
                // Let the main thread know about the update
                publish();
            }
            
            if(gameStepCounter >= maxGameSteps) keepRunning = false;
        }
        
        state.finished = true;
        
        publish();
        
        return null;
    }

    @Override
    protected void process(List<Void> dummy) {
        gamePanel.update();
    }
    
    private final static double MIN_GAME_STEP_INTERVAL_IN_SECS = 0.05;
    private final static double MAX_GAME_TIME_IN_SECS = 60;
    private GameState state;
    private GamePanel gamePanel;
    private MainWindow mainWindow;
    private boolean cancelRequested;
}
