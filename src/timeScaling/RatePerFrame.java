/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeScaling;

/**
 *
 * @author James
 */
public class RatePerFrame {
    public RatePerFrame(double value) {
        _value = value;
    }
    
    public void setValue(double value) {
        _value = value;
    }
    
    public double value() {
        return _value;
    }
    
    public RatePerSecond toPerSecond() {
        return new RatePerSecond(TimeScaler.perFrameToPerSecond(_value));
    }
    
    protected double _value;
}