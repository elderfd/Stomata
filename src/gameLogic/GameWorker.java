/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.GamePanel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author James
 */
public class GameWorker extends SwingWorker<Void, Void> {
    public GameWorker(GameState state, GamePanel gamePanel) {
        this.state = state;
        this.gamePanel = gamePanel;
    }
    
    @Override
    public Void doInBackground() {
        boolean keepRunning = true;
        long minGameStepIntervalInMillisecs = (long) (MIN_GAME_STEP_INTERVAL_IN_SECS * 1000);
        long timeOfLastGameStep = System.currentTimeMillis();
        int maxGameSteps = (int) (MAX_GAME_TIME_IN_SECS / MIN_GAME_STEP_INTERVAL_IN_SECS);
        int gameStepCounter = 0;
        
        while(keepRunning) {
            
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
        
        return null;
    }

    @Override
    protected void process(List<Void> dummy) {
        gamePanel.update();
    }
    
    final static double MIN_GAME_STEP_INTERVAL_IN_SECS = 1;
    final static double MAX_GAME_TIME_IN_SECS = 10;
    GameState state;
    GamePanel gamePanel;
}
