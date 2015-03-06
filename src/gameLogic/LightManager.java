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
import static java.lang.Math.abs;
import utility.Location;

/**
 *
 * @author James Elderfield
 */
public class LightManager {
    public LightManager(GameState state) {
        _state = state;
    }
    
    public Location getSunLocation(GamePanel panel) {
        double hour = _state.timeInDay();
        
        Location loc = null;
       
        int maxX = panel.coordTransform().gameWidthToScreenWidth(_state.getWidthOfArena());
        int minX = 0;
        int minY = panel.coordTransform().gameHeightToScreenHeight((int)(_state.getHeightOfArena() * 0.5));
        int maxY = 0;
        
        if(hour > sunrise && hour < sunset) {
            double xProportion = (hour - sunrise) / (sunset - sunrise);
            int x = (int)(maxX * xProportion + minX);
            
            double hoursFromMidPoint = abs((sunrise + (sunset - sunrise) / 2) - hour);
            double yProportion = hoursFromMidPoint / ((sunset - sunrise) / 2);
            int y = (int)(maxY + minY * yProportion * yProportion);
            
            loc = new Location(x, y);
        }
        
        return loc;
    }
    
    public double getBrightnessFactor() {
        double brightnessFactor;
        
        double timeToSetOrRise = 2; 
        
        // Should be dark at night
        if(_state.timeInDay() < sunrise || _state.timeInDay() > sunset) {
            brightnessFactor = 0;
        } else {
            double timeFromSunrise = _state.timeInDay() - sunrise;
            double timeFromSunset = sunset - _state.timeInDay();
            
            if(timeFromSunrise < timeToSetOrRise) {
                brightnessFactor = timeFromSunrise / timeToSetOrRise;
            } else if(timeFromSunset < timeToSetOrRise) {
                brightnessFactor = timeFromSunset / timeToSetOrRise;
            } else {
                brightnessFactor = 1;
            }
        }
        
        return brightnessFactor;
    }
    
    double sunrise = 6;
    double sunset = 21;
    GameState _state;
}
