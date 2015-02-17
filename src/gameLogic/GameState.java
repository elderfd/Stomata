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
import utility.RectangularArea;

/**
 * This class holds the state of a game at time t. Can be interrogated to get
 * the state and can calculate the state at time t + 1.
 * @author James
 */
public class GameState {
    public GameState() {
        stomata = new ArrayList<>();
        entities = new ArrayList<>();
        rng = new RNG();
        
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
        
        // Return entities second so they draw over the top
        for(Entity entity : entities) {
            returnList.add(entity);
        }
        
        return returnList;
    }
    
    public void reset() {
        entities.clear();
        populateRandomStomata();
        finished = false;
        
        points = 0;
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
    
    public Stoma getStomaAtLocation(Location location) {
        for(Stoma stoma : stomata) {
            if(stoma.getHitBox().containsLocation(location)) {
                return stoma;
            }
        }
        
        return null;
    }
    
    public void updateGameState () {
        // TODO: Better game logic
        
        // TODO: Put some collision-detection in
        
        // Move existing pathogens
        ArrayList<Integer> indicesToRemove = new ArrayList<>();
        int counter = 0;
        
        for(Entity entity : entities) {
            // Test for removal
            if(entity.shouldDie(rng)) {
                indicesToRemove.add(counter);
            } else {
                entity.updateLocation(rng);
            } 
            
            counter++;
        }
        
        Collections.sort(indicesToRemove, Collections.reverseOrder());
        
        for(int index : indicesToRemove) {
            entities.remove(index);
        }
        
        // Add new pathogens if we need to
        for(int i = 0; i < PATHOGEN_SPAWN_EVENT_ATTEMPTS; i++) {
            if(rng.bernoulliTrial(PATHOGEN_SPAWN_PROBABILITY)) {
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
                
                entities.add(new Pathogen(
                    spawnLocation,
                    target,
                    rng
                )
        );
            }
        }
        
        updatePoints();
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        ArrayList<Location> randomLocations = stomataArea.getUniqueListOfRandomLocations(NUM_STOMATA, rng);
        
        for(Location location : randomLocations) {
            stomata.add(new Stoma(location));
        }
    }
    
    public void updatePoints() {
        // Add some points for all open stomata
        int pointsPerOpenStoma = 10;
        
        for(Stoma stoma : stomata) {
            if(stoma.isOpen()) points += pointsPerOpenStoma;
        }
        
        // Remove points (and pathogens!) for any hits on target when stomata is open
        // TODO: Generalise this away from entities
        int pointCostPerPathogen = 20;
        
        ArrayList<Entity> entitiesToRemove = new ArrayList();
        
        for(Entity entity : entities) {
            Pathogen pathogen = (Pathogen)(entity);
            
            if(pathogen.hasHitTarget()) {
                Stoma stomaAtLocation = getStomaAtLocation(pathogen.getLocation());
                
                if(stomaAtLocation != null &&
                    stomaAtLocation.isOpen()
                ) {
                    points -= pointCostPerPathogen;
                }
                
                entitiesToRemove.add(entity);
            } 
        }
        
        entities.removeAll(entitiesToRemove);
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getWidthOfArena() {
        return ARENA_NUM_COLS;
    }
    
    public int getHeightOfArena() {
        return ARENA_NUM_ROWS;
    }
    
    private ArrayList<Stoma> stomata;
    private ArrayList<Entity> entities;
    
    private RNG rng;
    
    // Some constants to change later
    static final private int ARENA_NUM_COLS = 300;
    static final private int ARENA_NUM_ROWS = 225;
    static final private int NUM_STOMATA = 5;
    static final private double PATHOGEN_SPAWN_PROBABILITY = 0.5;
    static final private int PATHOGEN_SPAWN_EVENT_ATTEMPTS = 3;
    
    final private RectangularArea stomataArea;
    final private RectangularArea pathogenSpawnArea;
    
    // The number of points the player has earned
    private int points = 0;
    
    public boolean finished;
}
