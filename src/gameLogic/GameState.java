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
package gameLogic;

import distributions.ArbitraryDiscreteDistribution;
import distributions.DiscreteDistribution;
import functionalInterfaces.TriConsumer;
import gui.DrawableObject;
import static java.lang.Math.abs;
import utility.Location;
import utility.RNG;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import timeScaling.RatePerFrame;
import timeScaling.RatePerSecond;
import java.util.HashMap;
import utility.RectangularArea;

/**
 * This class holds the state of a game at time t. Can be interrogated to get
 * the state and can calculate the state at time t + 1.
 * @author James Elderfield
 */
public class GameState {
    public GameState() {
        stomata = new ArrayList<>();
        pathogens = new ArrayList<>();
        rng = new RNG();
        _pointsManager = new PointsManager(this);
        
        // Set up key areas in game arena
        // Stomata area is currently bottom half
        // Take into account height and width of stomata so they don't go off the edge
        Stoma testStoma = new Stoma(null);
        
        stomataArea = new RectangularArea(
                new Location(0, (int)(ARENA_NUM_ROWS / 2)),
                ARENA_NUM_COLS - testStoma.getWidth(),
                (int)(ARENA_NUM_ROWS / 2 - testStoma.getHeight())
        );
        
        // Pathogens can only spawn from top of screen
        pathogenSpawnArea = new RectangularArea(
                new Location(0, 0),
                ARENA_NUM_COLS,
                1
        );
        
        populateRandomStomata();
    }
        
    // This returns a list of everything to draw to rendering window
    public ArrayList<DrawableObject> getAllDrawableObjects() {
        ArrayList<DrawableObject> returnList = new ArrayList<>();
        
        // TODO: Add all the other things to draw
        
        for(Stoma stoma : stomata) {
            returnList.add(stoma);
        }
        
        // Return pathogens second so they draw over the top
        for(Pathogen pathogen : pathogens) {
            returnList.add(pathogen);
        }
        
        return returnList;
    }
    
    public void reset() {
        pathogens.clear();
        populateRandomStomata();
        finished = false;
        timeInHours = 0;
        
        _pointsManager.reset();
    }
    
    // Returns whether anything changed
    public boolean toggleAnyStomataAtLocation(Location location) {
        // Check if any stomata are desired location, and toggle if so
        Stoma foundStoma = getStomaAtLocation(location);
        
        if(foundStoma != null) {
            foundStoma.toggle();
            return true;
        }
        
        return false;
    }
    
    public ArrayList<Stoma> getStomata() {
        return stomata;
    }
    
    public double timeInDay() {
        return timeInHours % 24;
    }
    
    public double getBrightnessFactor() {
        double brightnessFactor;
        
        double sunrise = 6;
        double sunset = 21;
        double timeToSetOrRise = 2; 
        
        // Should be dark at night
        if(timeInDay() < sunrise || timeInDay() > sunset) {
            brightnessFactor = 0;
        } else {
            double timeFromSunrise = timeInDay() - sunrise;
            double timeFromSunset = sunset - timeInDay();
            
            if(timeFromSunrise < timeToSetOrRise) {
                brightnessFactor = timeFromSunrise / timeToSetOrRise;
            } else if(timeFromSunset < timeToSetOrRise) {
                brightnessFactor = timeFromSunset / timeToSetOrRise;
            } else {
                brightnessFactor = 1;
            }
        }
        
        return brightnessFactor;
    }
    
    public Stoma getStomaAtLocation(Location location) {
        for(Stoma stoma : stomata) {
            if(stoma.getHitBox().containsLocation(location)) {
                return stoma;
            }
        }
        
        return null;
    }
    
    public void updateGameState () {        
        // Move existing pathogens
        ArrayList<Integer> indicesToRemove = new ArrayList<>();
        int counter = 0;
        
        for(Pathogen pathogen : pathogens) {
            // Test for removal
            if(pathogen.shouldDie(rng)) {
                indicesToRemove.add(counter);
            } else {
                pathogen.updateLocation(rng);
            } 
            
            // Check for collision with target
            if(pathogen.finishedInfection()) {
                indicesToRemove.add(counter);
                
                // Only points lost if the stoma is open
                if(pathogen.getTarget().isOpen()) {
                    _pointsManager.infectionEvent();
                }
            }
            
            counter++;
        }
        
        Collections.sort(indicesToRemove, Collections.reverseOrder());
        
        for(int index : indicesToRemove) {
            pathogens.remove(index);
        }
        
        // Add new pathogens if we need to
        int numberOfNewPathogens = pathogenSpawnDistribution.getVariate(rng);
        
        if(numberOfNewPathogens > 0) {
            for(int i = 0; i < numberOfNewPathogens; i++) {
                // Spawn the pathogen at a random location
                Location spawnLocation = pathogenSpawnArea.getRandomLocationInArea(rng);

                // Target it at the stomata which is nearest horizontally
                int bestDistance = -1;
                Stoma target = null;

                for(Stoma stoma : stomata) {
                    int horizontalDistance = abs(stoma.getLocation().getX() - spawnLocation.getX());

                    if(bestDistance == -1 || horizontalDistance < bestDistance) {
                        bestDistance = horizontalDistance;
                        target = stoma;
                    }
                }

                pathogens.add(
                    new Pathogen(
                        spawnLocation,
                        target,
                        rng
                    )
                );
            }
        }   
        
        _pointsManager.tick();
        
        // Increase the time
        timeInHours += timeIncreaseRate.value();
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        // TODO: Stop them from overlapping
        
        ArrayList<Location> randomLocations = stomataArea.getUniqueListOfRandomLocations(NUM_STOMATA, rng);
        
        for(int i = 0; i < NUM_STOMATA; i++) {
            Location randomLocation = stomataArea.getRandomLocationInArea(rng);
            
            Predicate<Stoma> haveCreatedOverlap = (Stoma newStoma) -> {
                for(Stoma stoma : stomata) {
                    if(newStoma.getHitBox().overlapsWith((RectangularArea)stoma.getHitBox())) {
                        stomata.remove(newStoma);
                        return true;
                    }
                }
                
                return false;
            };
            
            // Try adding a stomata until we find a place where it doesn't overlap
            do {
                stomata.add(new Stoma(randomLocation));
            } while(!haveCreatedOverlap.test(stomata.get(stomata.size() - 1)));
            
        }
        
        for(Location location : randomLocations) {
            stomata.add(new Stoma(location));
        }
    }
    
    public PointsManager pointsManager() {
        return _pointsManager;
    }
    
    public int getWidthOfArena() {
        return ARENA_NUM_COLS;
    }
    
    public int getHeightOfArena() {
        return ARENA_NUM_ROWS;
    }
    
    private ArrayList<Stoma> stomata;
    private ArrayList<Pathogen> pathogens;
    
    private RNG rng;
    
    // Some constants to change later
    static final private int ARENA_NUM_COLS = 300;
    static final private int ARENA_NUM_ROWS = 225;
    static final private int NUM_STOMATA = 5;
    static final private DiscreteDistribution pathogenSpawnDistribution = 
        new ArbitraryDiscreteDistribution(
            new HashMap<Integer, Double>(){{
                Double totalProbability = 0.0;
                
                TriConsumer<Integer, RatePerSecond, Double> addRate =
                    (Integer value, RatePerSecond probabilityPerSecond, Double total) -> {
                        RatePerFrame probabilityPerFrame = probabilityPerSecond.toPerFrame();

                        total += probabilityPerFrame.value();
                        put(value, probabilityPerFrame.value());
                    };
                
                addRate.accept(10, new RatePerSecond(0.05), totalProbability);
                addRate.accept(5, new RatePerSecond(0.1), totalProbability);
                addRate.accept(1, new RatePerSecond(0.5), totalProbability);
                        
                // Rest of probability should be 0
                put(0, 1 - totalProbability);
            }}
        );
    
    final private RectangularArea stomataArea;
    final private RectangularArea pathogenSpawnArea;
    
    public boolean finished;
    
    public double timeInHours = 0;
    public RatePerFrame timeIncreaseRate = new RatePerSecond(2).toPerFrame();
    
    private PointsManager _pointsManager;
}
