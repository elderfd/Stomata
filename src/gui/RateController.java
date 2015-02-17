/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author James
 */
public class RateController {
    public RateController(GamePanel panel) {
        this.panel = panel;
    }
    
    public int scaleFromPerSecondToPerFrame(int perSecondQuantity) {
        return perSecondQuantity / (int) targetFPS;
    }
    
    public double scaleFromPerSecondToPerFrame(double perSecondQuantity) {
        return perSecondQuantity / targetFPS;
    }
    
    public int scaleFromPerFrameToPerSecond(int perFrameQuantity) {
        return perFrameQuantity / (int) targetFPS;
    }
    
    public double scaleFromPerFrameToPerSecond(double perFrameQuantity) {
        return perFrameQuantity / targetFPS;
    }
    
    private GamePanel panel;
    private double targetFPS = 30;
}
