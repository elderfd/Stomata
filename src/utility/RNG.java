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
        int rNum = nextInt(max-min);
        
        return rNum + min;
    }
}
