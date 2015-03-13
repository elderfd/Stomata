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

/**
 *
 * @author James Elderfield
 */
public class RectangularArea implements Area {
    public RectangularArea(Location upperLeft, int width, int height) {
        this.upperLeft = upperLeft;
        this._width = width;
        this._height = height;
    }
    
    public Location getUpperLeft() {
        return upperLeft;
    }
    
    @Override
    public Location getRandomLocationInArea(RNG rng) {
        int x = rng.uniformIntInRange(upperLeft.getX(), upperLeft.getX() + _width);
        int y = rng.uniformIntInRange(upperLeft.getY(), upperLeft.getY() + _height);
        
        return new Location(x, y);
    }
    
    @Override
    public ArrayList<Location> getUniqueListOfRandomLocations(int numberOfLocations, RNG rng) {
        ArrayList<Location> returnList = new ArrayList();
        ArrayList<Location> locationsStillAvailable = new ArrayList<>();
        
        for(int x = upperLeft.getX(); x < upperLeft.getX() + _width; x++) {
            for(int y = upperLeft.getY(); y < upperLeft.getY() + _height; y++) {
                locationsStillAvailable.add(new Location(x, y));
            }
        }
        
        for(int i = 0; i < numberOfLocations && locationsStillAvailable.size() > 0; i++) {
            int randomIndex = rng.uniformIntInRange(0, locationsStillAvailable.size());
            
            returnList.add(locationsStillAvailable.get(randomIndex));
            locationsStillAvailable.remove(randomIndex);
        }
        
        return returnList;
    }
    
    @Override
    public boolean containsLocation(Location location) {
        return location.getX() > upperLeft.getX() &&
                location.getX() < upperLeft.getX() + _width &&
                location.getY() > upperLeft.getY() &&
                location.getY() < upperLeft.getY() + _height;
    }
    
    @Override
    public Location getCentroid() {
        int x = upperLeft.getX() + _width / 2;
        int y = upperLeft.getY() + _height / 2;
        
        return new Location(x, y);
    }
    
    @Override
    public boolean overlapsWith (RectangularArea other) {
        return !(upperLeft.getX() > other.upperLeft.getX() + other._width
                || upperLeft.getX() + _width < other.upperLeft.getX())
                && !(upperLeft.getY() > other.upperLeft.getY() + other._height
                || upperLeft.getY() + _height < other.upperLeft.getY());
    }
    
    public int width() {
        return _width;
    }
    
    public int height() {
        return _height;
    }
    
    public int area() {
        return _width * _height;
    }
    
    private Location upperLeft;
    private int _width;
    private int _height;
}
