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
        // TODO: Put something meaninful in here rather than this test code
        entities.add(new Pathogen(
                new Location(new Location(0,0),
                    ARENA_NUM_ROWS,
                    ARENA_NUM_COLS,
                    rng
                ),
            stomata.get(0))
        );
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
    static final private int ARENA_NUM_COLS = 5;
    static final private int ARENA_NUM_ROWS = 5;
    static final private int NUM_STOMATA = 5;
}
