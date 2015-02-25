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

import utility.RNG;

/**
 *
 * @author James Elderfield
 */
public class PoissonDistribution implements DiscreteDistribution {
    public PoissonDistribution(double mean) {
        _mean = mean;
    }
    
    public double mean() {
        return _mean;
    }
    
    @Override
    public int getVariate(RNG rng) {
        int numberOfEvents = 0;
        double target = Math.exp(-_mean); 
        double productSoFar = 1;
        
        while(productSoFar > target) {
            numberOfEvents ++;
            productSoFar *= rng.nextDouble();
        }
        
        return numberOfEvents - 1;
    }
    
    double _mean;
}
