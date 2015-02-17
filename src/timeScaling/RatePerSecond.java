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
public class RatePerSecond<T extends Number> {
    public RatePerSecond(T value) {
        _value = value;
    }
    
    public void setValue(T value) {
        _value = value;
    }
    
    public T value() {
        return _value;
    }
    
    public RatePerFrame toPerFrame() {
        return new RatePerFrame(_value);
    }
    
    protected T _value;
}
