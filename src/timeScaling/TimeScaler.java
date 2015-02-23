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
class TimeScaler {
    public static int perFrameToPerSecond(int perFrameRate) {
        return perFrameRate / targetFPS;
    }
    
    public static double perFrameToPerSecond(double perFrameRate) {
        return perFrameRate / (double)targetFPS;
    }    
    
    public static int perSecondToPerFrame(int perSecondRate) {
        return perSecondRate * targetFPS;
    }
    
    public static double perSecondToPerFrame(double perSecondRate) {
        return perSecondRate * targetFPS;
    }
    
    public static double secondsToFrames(double seconds) {
        return seconds * targetFPS;
    }
    
    public static double framesToSeconds(double frames) {
        return frames / targetFPS;
    }
    
    static private int targetFPS = 30;
}
