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
import gui.PathogenDustCloud;
import gui.PathogenInfectionEvent;
import gui.SpriteManager;
import gui.VisualEffect;
import java.awt.image.BufferedImage;
import static java.lang.Math.abs;
import utility.Location;
import utility.RNG;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import timeScaling.RatePerFrame;
import timeScaling.RatePerSecond;
import java.util.HashMap;
import utility.Area;
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
        
        int stomataAreaXBorderLeft = ARENA_NUM_COLS / 3;
        int stomataAreaXBorderRight = stomataAreaXBorderLeft - ARENA_NUM_COLS / 6; 
        int stomataAreaYBorder = ARENA_NUM_ROWS / 10;
        
        stomataArea = new RectangularArea(
            new Location(stomataAreaXBorderLeft, (int)(ARENA_NUM_ROWS / 2)),
            ARENA_NUM_COLS - testStoma.getWidth() - stomataAreaXBorderLeft - stomataAreaXBorderRight,
            (int)(ARENA_NUM_ROWS / 2 - testStoma.getHeight()) - stomataAreaYBorder
        );      
        
        // Pathogens can only spawn from top of screen
        pathogenSpawnArea = new RectangularArea(
                new Location(0, 0),
                ARENA_NUM_COLS,
                1
        );
        
        populateRandomStomata();
    }
        
    public void attachSpriteManager(SpriteManager manager) {
        _spriteManager = manager;
    }
    
    public boolean isLocationOnLeaf(Location location) {
        // TODO: This was added last minute - needs to be cleaned up
        // Get the leaf image
        BufferedImage leafImage = (BufferedImage)_spriteManager.getSpriteImage("leaf");
        
        // Map the location to a pixel on the image
        int leafCornerY = 3 * ARENA_NUM_ROWS / 7;
        int leafHeight = ARENA_NUM_ROWS - leafCornerY;
        int leafWidth = ARENA_NUM_COLS;
        
        int pixelX = (int)(leafImage.getWidth(null) * (double)location.getX() / (double)leafWidth);
        int pixelY = (int)((double)(location.getY() - leafCornerY) * leafImage.getHeight(null) / (double)leafHeight);
        
        // Return if the pixel is opaque or not
        int alpha = 0;
        
        if(pixelX >= 0 && pixelY >=0 && pixelX < leafImage.getWidth() && pixelY < leafImage.getHeight()) {
            alpha = (leafImage.getRGB(pixelX, pixelY) >> 24) & 0xff;
        }
        
        return alpha > 0;
    }
    
    // This returns a list of everything to draw to rendering window
    public ArrayList<DrawableObject> getAllDrawableObjects() {
        ArrayList<DrawableObject> returnList = new ArrayList<>();
        
        for(Stoma stoma : stomata) {
            returnList.add(stoma);
        }
        
        // Return pathogens second so they draw over the top
        for(Pathogen pathogen : pathogens) {
            returnList.add(pathogen);
        }
        
        return returnList;
    }
    
    public ArrayList<VisualEffect> getVisualEffects() {
        return effects;
    }
    
    public void updateEffects() {
        effects.removeIf((effect) -> effect.checkExpiration());
    }
    
    public void reset() {
        pathogens.clear();
        effects.clear();
        populateRandomStomata();
        finished = false;
        _timeInHours = 0;
        
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
        return _timeInHours % 24;
    }
    
    public Stoma getStomaAtLocation(Location location) {
        for(Stoma stoma : stomata) {
            if(stoma.getHitBox().containsLocation(location)) {
                return stoma;
            }
        }
        
        return null;
    }
    
    public void addVisualEffect(VisualEffect effect) {
        effects.add(effect);
    }
    
    public void updateGameState () {        
        // Move existing pathogens
        ArrayList<Integer> indicesToRemove = new ArrayList<>();
        int counter = 0;
        
        for(Pathogen pathogen : pathogens) {
            // Test for removal
            if(pathogen.shouldDie(rng, this)) {
                indicesToRemove.add(counter);
                addVisualEffect(new PathogenDustCloud(pathogen.getLocation()));
            } else {
                pathogen.updateLocation(rng, this);
            } 
            
            // Check for collision with target
            if(pathogen.finishedInfection()) {
                indicesToRemove.add(counter);
                
                // Only points lost if the stoma is open
                if(pathogen.getTarget().isOpen()) {
                    _pointsManager.infectionEvent();
                    addVisualEffect(new PathogenInfectionEvent(pathogen.getLocation()));
                } else {
                    addVisualEffect(new PathogenDustCloud(pathogen.getLocation()));
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
        _timeInHours += timeIncreaseRate.value();
    }
    
    // Populates the arena randomly with stomata
    private void populateRandomStomata() {
        stomata.clear();
        
        Predicate<Stoma> haveCreatedOverlap = (Stoma newStoma) -> {
            for(Stoma stoma : stomata) {
                if(newStoma != stoma && newStoma.getHitBox().overlapsWith((RectangularArea)stoma.getHitBox())) {
                    stomata.remove(newStoma);
                    return true;
                }
            }

            return false;
        };
        
        for(int i = 0; i < NUM_STOMATA; i++) {
            // Try adding a stomata until we find a place where it doesn't overlap
            do {
                Location randomLocation = stomataArea.getRandomLocationInArea(rng);
                stomata.add(new Stoma(randomLocation));
            } while(haveCreatedOverlap.test(stomata.get(stomata.size() - 1)));
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
    
    public double timeInHours() {
        return _timeInHours;
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
    
    public LightManager lightManager() {
        return _lightMangager;
    }
    
    final private Area stomataArea;
    final private RectangularArea pathogenSpawnArea;
    
    public boolean finished;
    
    public double _timeInHours = 0;
    public RatePerFrame timeIncreaseRate = new RatePerSecond(1).toPerFrame();
    
    private PointsManager _pointsManager;
    private ArrayList<VisualEffect> effects = new ArrayList<>();
    
    private LightManager _lightMangager = new LightManager(this);
    
    SpriteManager _spriteManager;
}
