/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author James
 */
public class PoissonDistribution {
    public PoissonDistribution(double mean) {
        _mean = mean;
    }
    
    public double mean() {
        return _mean;
    }
    
    double _mean;
}
