/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gui.DrawableObject;
import utility.Location;

/**
 *
 * @author James
 */
public abstract class Entity implements DrawableObject {
    public Entity(Location startLocation) {
        this.currentLocation = startLocation;
    }

    protected Location currentLocation;
}
