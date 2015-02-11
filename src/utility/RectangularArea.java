/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.ArrayList;

/**
 *
 * @author James
 */
public class RectangularArea implements Area {
    public RectangularArea(Location upperLeft, int width, int height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public Location getRandomLocationInArea(RNG rng) {
        int x = rng.uniformIntInRange(upperLeft.getX(), upperLeft.getX() + width);
        int y = rng.uniformIntInRange(upperLeft.getY(), upperLeft.getY() + height);
        
        return new Location(x, y);
    }
    
    @Override
    public ArrayList<Location> getUniqueListOfRandomLocations(int numberOfLocations, RNG rng) {
        ArrayList<Location> returnList = new ArrayList();
        ArrayList<Location> locationsStillAvailable = new ArrayList<>();
        
        for(int x = upperLeft.getX(); x < upperLeft.getX() + width; x++) {
            for(int y = upperLeft.getY(); y < upperLeft.getY() + height; y++) {
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
                location.getX() < upperLeft.getX() + width &&
                location.getY() > upperLeft.getY() &&
                location.getY() < upperLeft.getY() + height;
    }
    
    @Override
    public Location getCentroid() {
        int x = upperLeft.getX() + width / 2;
        int y = upperLeft.getY() + height / 2;
        
        return new Location(x, y);
    }
    
    // TODO: Would be good to implement overlap checking with any type of area
    public boolean overlapsWith (RectangularArea other) {
        return !(upperLeft.getX() > other.upperLeft.getX() + other.width
                || upperLeft.getX() + width < other.upperLeft.getX()
                || upperLeft.getY() < other.upperLeft.getY() + other.height
                || upperLeft.getY() + height < other.upperLeft.getY());
    }
    
    
    private Location upperLeft;
    private int width;
    private int height;
}
