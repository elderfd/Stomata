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
public interface Area {
    public boolean containsLocation(Location location);
    public Location getRandomLocationInArea(RNG rng);
    public ArrayList<Location> getUniqueListOfRandomLocations(int numberOfLocations, RNG rng);
    public Location getCentroid();
    public boolean overlapsWith(RectangularArea other);
}
