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
package distributions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import utility.RNG;

/**
 *
 * @author James Elderfield
 */
public class ArbitraryDiscreteDistribution implements DiscreteDistribution {
    public ArbitraryDiscreteDistribution(Map<Integer, Double> valueToWeightingMap) {
        _valueToWeightingMap = new LinkedHashMap<>();
        
        for(Map.Entry<Integer, Double> entry : valueToWeightingMap.entrySet()) {
            _valueToWeightingMap.put(entry.getKey(), new Weighting(entry.getValue()));
        }
        
        recalculateWeightings();
    }
    
    @Override
    public int getVariate(RNG rng) {
        double randValue = rng.nextDouble();
        double sumSoFar = 0;
        int retVal = -1;
        
        Iterator<Map.Entry<Integer, Weighting>> it = _valueToWeightingMap.entrySet().iterator();
        
        while(it.hasNext()) {
            Map.Entry<Integer, Weighting> entry = it.next();
            
            sumSoFar += entry.getValue().normalised;
            
            if(sumSoFar > randValue) {
                retVal = entry.getKey();
                break;
            }
        }
        
        return retVal;
    }
    
    public void recalculateWeightings() {
        double cumulativeValues = 0; 
        
        Iterator<Map.Entry<Integer, Weighting>> it = _valueToWeightingMap.entrySet().iterator();
        
        while(it.hasNext()) {
            Map.Entry<Integer, Weighting> entry = it.next();
            
            if(entry.getValue().raw == 0) {
                it.remove();
            } else {
                cumulativeValues += entry.getValue().raw;
            }
        }
        
        it = _valueToWeightingMap.entrySet().iterator();
        
        while(it.hasNext()) {
            Map.Entry<Integer, Weighting> entry = it.next();
            
            entry.getValue().normalised = entry.getValue().raw / cumulativeValues;
        }
    }
    
    private class Weighting {
        public Weighting(double raw) {
            this.raw = raw;
        }
        
        public double raw;
        public double normalised;
    }
    
    private LinkedHashMap<Integer, Weighting> _valueToWeightingMap;
}
