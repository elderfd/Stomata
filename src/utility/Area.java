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
    boolean containsLocation(Location location);
    Location getRandomLocationInArea(RNG rng);
    ArrayList<Location> getUniqueListOfRandomLocations(int numberOfLocations, RNG rng);
}
