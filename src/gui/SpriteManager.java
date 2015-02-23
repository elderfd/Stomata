/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

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
        // Utility class
        class SpriteIDAndFileName {
            SpriteIDAndFileName(String ID, String fileName) {
                _ID = ID;
                _fileName = fileName;
            }
            
            public String ID() {
                return _ID;
            }
            
            public String fileName() {
                return _fileName;
            }
            
            String _ID;
            String _fileName;
        }
        
        ArrayList<SpriteIDAndFileName> spriteIDsAndFileNames = new ArrayList<>();
        
        // Lambda to neaten up syntax
        BiConsumer<String, String> addIDFileNamePair = (String ID, String fileName) -> {
            spriteIDsAndFileNames.add(new SpriteIDAndFileName(ID, fileName));
        };
        
        // Add all the data we'll need
        addIDFileNamePair.accept("stomaOpen", "stomaOpen.png");
        addIDFileNamePair.accept("stomaClosed", "stomaClosed.png");
        addIDFileNamePair.accept("background", "background.jpg");
        addIDFileNamePair.accept("pathogen", "EvileSpore.png");
        
        String imageFolderName = "images/";
        
        for(SpriteIDAndFileName pair : spriteIDsAndFileNames) {
            Sprite newSprite = new Sprite();
            
            try {
                newSprite.loadFromFile(imageFolderName + pair.fileName());
            } catch(IOException e) {
                try {
                    newSprite.loadFromFile(imageFolderName + placeholderFileName);
                } catch(IOException e2) {
                    // If here then all hell is breaking loose so just give up
                    return;
                }
            }
            
            nameToSpriteMap.put(pair.ID(), newSprite);
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
