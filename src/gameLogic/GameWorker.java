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
package gameLogic;

import gui.GamePanel;
import gui.MainWindow;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author James Elderfield
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
    private final static double MAX_GAME_TIME_IN_SECS = 1;
    private GameState state;
    private GamePanel gamePanel;
    private MainWindow mainWindow;
    private boolean cancelRequested;
}
