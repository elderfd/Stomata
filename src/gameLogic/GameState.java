/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import utility.Location;
import utility.RNG;
import java.util.ArrayList;
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
        // Stomata area is currently bottom half and pathogens top half
        stomataArea = new RectangularArea(new Location(0, (int)(ARENA_NUM_COLS / 2)),
                ARENA_NUM_COLS,
                (int)(ARENA_NUM_ROWS / 2)
        );
        
        pathogenSpawnArea = new RectangularArea(new Location(0, 0),
                ARENA_NUM_COLS,
                (int)(ARENA_NUM_ROWS / 2)
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
    
    public void updateGameState () {
        // TODO: Better game logic
        
        // TODO: Put some collision-detection in
        
        // Move existing pathogens
        for(Entity entity : entities) {
            // Currently ok to do this cast as all entities pathogens, not true in future!
            Pathogen pathogen = (Pathogen)(entity);
            
            // Move pathogen one square closer to target
            Location target = pathogen.getTargetLocation();
            Location current = pathogen.getLocation();
            
            // TODO: Make moving better
            
            // Check it hasn't already reached its target
            if(!target.equals(current)) {
                int newX = current.getX(), newY = current.getY();
                
                int xDiff = target.getX() - current.getX();
                int yDiff = target.getY() - current.getY();
                
                if(xDiff > 0) {
                    newX++;
                } else if(xDiff < 0) {
                    newX--;
                }
                
                if(yDiff > 0) {
                    newY++;
                } else if(yDiff < 0) {
                    newY--;
                }
                
                pathogen.setLocation(new Location(newX, newY));
            }
        }
        
        // Add new pathogens if we need to
        for(int i = 0; i < PATHOGEN_SPAWN_EVENT_ATTEMPTS; i++) {
            if(rng.bernoulliTrial(PATHOGEN_SPAWN_PROBABILITY)) {
                entities.add(new Pathogen(
                pathogenSpawnArea.getRandomLocationInArea(rng),
            stomata.get(rng.uniformIntInRange(0, stomata.size())))
        );
            }
        }
        
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        ArrayList<Location> randomLocations = stomataArea.getUniqueListOfRandomLocations(NUM_STOMATA, rng);
        
        for(Location location : randomLocations) {
            stomata.add(new Stoma(location));
        }
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
    static final private int ARENA_NUM_COLS = 10;
    static final private int ARENA_NUM_ROWS = 8;
    static final private int NUM_STOMATA = 5;
    static final private double PATHOGEN_SPAWN_PROBABILITY = 0.5;
    static final private int PATHOGEN_SPAWN_EVENT_ATTEMPTS = 3;
    
    final private RectangularArea stomataArea;
    final private RectangularArea pathogenSpawnArea;
}