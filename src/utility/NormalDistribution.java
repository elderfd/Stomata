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
public class NormalDistribution {
    public NormalDistribution(double mean, double std) {
        _mean = mean;
        _std = std;
    }
    
    public double mean() {
        return _mean;
    }
    
    public double std() {
        return _std;
    }
    
    private double _mean;
    private double _std;
}
