/* 
 * The MIT License
 *
 * Copyright 2015 James.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
