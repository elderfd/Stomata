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
package utility;

import java.util.Random;

/**
 *
 * @author James
 */
public class RNG extends Random {
    public int uniformIntInRange(int min, int max) {
        int rNum = nextInt(max - min);
        
        return rNum + min;
    }
    
    public boolean bernoulliTrial(double probabilityOfSuccess) {
        return (nextDouble() < probabilityOfSuccess);
    }
    
    public double getNormalVariate(NormalDistribution distribution) {
        return nextGaussian() * distribution.std() + distribution.mean();
    }
    
    public int getBinomialVariate(BinomialDistribution distribution) {
        int successes = 0;
        
        for(int i = 0; i < distribution.k(); i++) {
            if(bernoulliTrial(distribution.p())) {
                successes++;
            }
        }
        
        return successes;
    }
    
    public int getPoissonVariate(PoissonDistribution distribution) {
        int numberOfEvents = 0;
        double target = Math.exp(-distribution.mean()); 
        double productSoFar = 1;
        
        while(productSoFar > target) {
            numberOfEvents ++;
            productSoFar *= nextDouble();
        }
        
        return numberOfEvents - 1;
    }
}
