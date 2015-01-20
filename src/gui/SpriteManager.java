/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author James
 */
public class SpriteManager {
    public SpriteManager() {
        nameToSpriteMap = new HashMap<>();
        loadStandardSprites();
    }
    
    private void loadStandardSprites() {
        List<String> spriteIDs = Arrays.asList(
            "stoma",
            "background",
            "pathogen"
        );
        List<String> fileNames = Arrays.asList(
            "stoma.jpg",
            "background.jpg",
            "pathogen.jpg"
        );
        
        String imageFolderName = "images/";
        
        for(int i = 0; i < spriteIDs.size(); i++) {
            Sprite newSprite = new Sprite();
            
            try {
                newSprite.loadFromFile(imageFolderName + fileNames.get(i));
            } catch(IOException e) {
                try {
                    newSprite.loadFromFile(imageFolderName + placeholderFileName);
                } catch(IOException e2) {
                    // If here then all hell is breaking loose so just give up
                    return;
                }
            }
            
            nameToSpriteMap.put(spriteIDs.get(i), newSprite);
        }       
    }
    
    public Image getSpriteImage(String spriteID) {
        Image retImage = null;
        
        try {
            retImage = nameToSpriteMap.get(spriteID).getImage();
        } catch(Exception e) {
            // Should never happen
            // TODO: Better error handling here
            
            System.exit(1);
        }
        
        return retImage;
    }
    
    private HashMap<String, Sprite> nameToSpriteMap;
    private final static String placeholderFileName = "placeholder.jpg";
}
