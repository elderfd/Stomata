/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import timeScaling.RatePerFrame;
import timeScaling.RatePerSecond;

/**
 *
 * @author James
 */
public class PointsManager {
    public PointsManager(GameState state) {
        _state = state;
    }
    
    public void infectionEvent() {
        _points -= pointsLostPerPathogenInfection;
    }
    
    public double points() {
        return _points;
    }
    
    public void tick() {
        // Interrogate the state to work out how many points to add
        for(Stoma stoma : _state.getStomata()) {
            if(stoma.isOpen()) {
                // TODO: Account for light
                _points += maxPointsGainPerOpenStomata.value();
            }
        }
    }
    
    public void reset() {
        _points = 0;
    }
    
    private double _points = 0;
    
    // The state the manager is attached to
    GameState _state;
    
    // Points modifiers
    private RatePerFrame maxPointsGainPerOpenStomata = new RatePerSecond(1).toPerFrame();
    private double pointsLostPerPathogenInfection = 5;
}
