/*
 * The MIT License
 *
 * Copyright 2015 James Elderfield.
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
package utility;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James Elderfield
 */
public class CompositeRectangularArea implements Area {
    public CompositeRectangularArea(List<RectangularArea> components) {
        this._components = components;
    }
    
    @Override
    public Location getRandomLocationInArea(RNG rng) {
        // Weight by area
        int totalArea = 0;
        
        for(RectangularArea component : _components) {
            totalArea += component.area();
        }
        
        // Choose an area
        double randomNumber = rng.uniformIntInRange(0, totalArea);
        int cumSum = 0;
        int chosenIndex;
        
        for(chosenIndex = 0; chosenIndex < _components.size() - 1 && randomNumber <= cumSum; chosenIndex++) {
            cumSum += _components.get(chosenIndex).area();
        }
                
        return _components.get(chosenIndex).getRandomLocationInArea(rng);
    }
    
    @Override
    public ArrayList<Location> getUniqueListOfRandomLocations(int numberOfLocations, RNG rng) {
        ArrayList<Location> returnList = new ArrayList();
        
        for(int i = 0; i < numberOfLocations; i++) {
            Location randomLocation;
            
            do {
                randomLocation = getRandomLocationInArea(rng);
            } while(!returnList.contains(randomLocation));
            
            returnList.add(randomLocation);
        }
        
        return returnList;
    }
    
    @Override
    public boolean containsLocation(Location location) {
        for(RectangularArea component : _components) {
            if(component.containsLocation(location)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public Location getCentroid() {
        // Return average centroid
        Location centroid = new Location(0, 0);
        int xTotal = 0;
        int yTotal = 0;
        
        for(RectangularArea component : _components) {
            Location subCentroid = component.getCentroid();
        
            xTotal += subCentroid.getX();
            yTotal += subCentroid.getY();
        }
        
        centroid.setX(xTotal / _components.size());
        centroid.setY(yTotal / _components.size());
        
        return centroid;
    }
    
    @Override
    public boolean overlapsWith (RectangularArea other) {
        for(RectangularArea component : _components) {
            if(component.overlapsWith(other)) {
                return true;
            }
        }
        
        return false;
    }
    
    private List<RectangularArea> _components;
}
