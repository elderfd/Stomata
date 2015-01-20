/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicstest;

import java.util.ArrayList;

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
        
        populateRandomStomata();
    }
    
    // Represents any temporary entity in the game
    abstract private class Entity implements DrawableObject {
        public Entity(Location startLocation) {
            this.currentLocation = startLocation;
        }
        
        protected Location currentLocation;
    }
    
    private class Pathogen extends Entity {
        public Pathogen(Location startLocation, Stoma target) {
            super(startLocation);
            this.targetLocation = target.getLocation();
        }
        
        public Location getLocation() {
            return currentLocation;
        }
        
        public String getSpriteID() {
            return spriteID;
        }
        
        // The target the pathogen is heading for
        private Location targetLocation;
        private String spriteID = "pathogen"; // TODO: Bring in an image for this
    }
    
    private class Stoma implements DrawableObject {
        public Stoma(Location location) {
            this.location = location;
        }
        
        public Location getLocation() {
            return location;
        }
        
        public String getSpriteID() {
            return spriteID;
        }
        
        // Whether or not the stoma is currently open
        private boolean open = false;
        
        // Where the stoma is located in space
        private Location location;
        
        private String spriteID = "stoma";
    }
    
    // This returns a list of everything to draw to rendering window
    ArrayList<DrawableObject> getAllDrawableObjects() {
        ArrayList<DrawableObject> returnList = new ArrayList<>();
        
        // TODO: Add all the other things to draw
        
        for(Stoma stoma : stomata) {
            returnList.add(stoma);
        }
        
        return returnList;
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        Location topLeftOfArena = new Location(0, 0);
        ArrayList<Location> locationsStillAvailable = new ArrayList<>();
        
        for(int x = 0; x < ARENA_NUM_COLS; x++) {
            for(int y = 0; y < ARENA_NUM_ROWS; y++) {
                locationsStillAvailable.add(new Location(x, y));
            }
        }
        
        for(int i = 0; i < NUM_STOMATA && locationsStillAvailable.size() > 0; i++) {
            int randomIndex = rng.uniformIntInRange(0, locationsStillAvailable.size());
            
            stomata.add(new Stoma(locationsStillAvailable.get(randomIndex)));
            locationsStillAvailable.remove(randomIndex);
        }
    }
    
    public int getWidthOfArena() {
        return ARENA_NUM_COLS;
    }
    
    public int getHeightOfArena() {
        return ARENA_NUM_ROWS;
    }
    
    private int time = 0;
    private ArrayList<Stoma> stomata;
    private ArrayList<Entity> entities;
    
    private RNG rng;
    
    // Some constants to change later
    static final int ARENA_NUM_COLS = 5;
    static final int ARENA_NUM_ROWS = 5;
    static final int NUM_STOMATA = 5;
}
