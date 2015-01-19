/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicstest;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author James
 */
public class Sprite {
    
    public void loadFromFile(String fileName) throws IOException {
        image = ImageIO.read(new File(fileName));
        initialised = true;
    }
    
    public Image getImage() throws Exception {
        if(!initialised) {
            throw new Exception("Error: Attempted to access uninitialised sprite.");
        }
        
        return image;
    }
    
    private boolean initialised;
    private Image image;
}
