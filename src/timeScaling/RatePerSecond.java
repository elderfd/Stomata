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
public class RatePerSecond {
    public RatePerSecond(double value) {
        _value = value;
    }
    
    public void setValue(double value) {
        _value = value;
    }
    
    public double value() {
        return _value;
    }
    
    public RatePerFrame toPerFrame() {
        return new RatePerFrame(TimeScaler.perSecondToPerFrame(_value));
    }
    
    protected double _value;
}
