/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
