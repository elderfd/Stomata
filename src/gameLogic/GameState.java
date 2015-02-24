/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import static java.lang.Math.abs;
import utility.Location;
import utility.RNG;
import java.util.ArrayList;
import java.util.Collections;
import timeScaling.RatePerSecond;
import utility.PoissonDistribution;
import utility.RectangularArea;

/**
 * This class holds the state of a game at time t. Can be interrogated to get
 * the state and can calculate the state at time t + 1.
 * @author James
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
            if(pathogen.hasHitTarget()) {
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
        int numberOfNewPathogens = rng.getPoissonVariate(pathogenSpawnDistribution);
        
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
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        ArrayList<Location> randomLocations = stomataArea.getUniqueListOfRandomLocations(NUM_STOMATA, rng);
        
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
    static final private PoissonDistribution pathogenSpawnDistribution = 
        new PoissonDistribution(
            new RatePerSecond(4).toPerFrame().value()
        );
    
    final private RectangularArea stomataArea;
    final private RectangularArea pathogenSpawnArea;
    
    public boolean finished;
    
    private PointsManager _pointsManager;
}
