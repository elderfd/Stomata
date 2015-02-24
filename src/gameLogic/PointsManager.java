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

import timeScaling.RatePerFrame;
import timeScaling.RatePerSecond;

/**
 *
 * @author James Elderfield
 */
public class PointsManager {
    public PointsManager(GameState state) {
        _state = state;
    }
    
    public void infectionEvent() {
        _points -= pointsLostPerPathogenInfection;
    }
    
    public double points() {
        return _points;
    }
    
    public void tick() {
        // Interrogate the state to work out how many points to add
        double brightness = _state.getBrightnessFactor();
        
        for(Stoma stoma : _state.getStomata()) {
            if(stoma.isOpen()) {
                _points += maxPointsGainPerOpenStomata.value() * brightness;
            }
        }
    }
    
    public void reset() {
        _points = 0;
    }
    
    private double _points = 0;
    
    // The state the manager is attached to
    GameState _state;
    
    // Points modifiers
    private RatePerFrame maxPointsGainPerOpenStomata = new RatePerSecond(1).toPerFrame();
    private double pointsLostPerPathogenInfection = 5;
}
