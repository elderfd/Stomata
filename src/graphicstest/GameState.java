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
    
    private class Location {
        // Produces location at set location
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        // Produces a random location within limits
        public Location(Location lowerLeft, int height, int width, RNG rng) {
            this.x = lowerLeft.getX() + rng.uniformIntInRange(0, width);
            this.y = lowerLeft.getY() + rng.uniformIntInRange(0, height);
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        private int x;
        private int y;
    }
    
    // Represents any temporary entity in the game
    private class Entity {
        public Entity(Location startLocation) {
            this.currentLocation = startLocation;
        }
        
        private Location currentLocation;
    }
    
    private class Pathogen extends Entity {
        public Pathogen(Location startLocation, Stoma target) {
            super(startLocation);
            this.targetLocation = target.getLocation();
        }
        
        // The target the pathogen is heading for
        Location targetLocation;
    }
    
    private class Stoma {
        public Stoma(Location location) {
            this.location = location;
        }
        
        Location getLocation() {
            return location;
        }
        
        // Whether or not the stoma is currently open
        private boolean open = false;
        
        // Where the stoma is located in space
        private Location location;
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        Location lowerLeftOfArena = new Location(0, 0);
        
        for(int i = 0; i < NUM_STOMATA; i++) {
            Location randomLocation = new Location (
                lowerLeftOfArena,
                ARENA_NUM_ROWS,
                ARENA_NUM_COLS,
                rng
            );
            
            stomata.add(new Stoma(randomLocation));
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
    static final int ARENA_NUM_COLS = 10;
    static final int ARENA_NUM_ROWS = 10;
    static final int NUM_STOMATA = 20;
}
