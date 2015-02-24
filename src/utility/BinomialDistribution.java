/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static java.lang.Math.ceil;

/**
 *
 * @author James
 */
public class BinomialDistribution {
    public BinomialDistribution(double p, int k) {
        _p = p;
        _k = k;
    }
    
    public double p() {
        return _p;
    }
    
    public int k() {
        return _k;
    }
    
    int _k;
    double _p;
}
