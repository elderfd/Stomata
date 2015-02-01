/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import utility.Area;
import utility.Location;

/**
 *
 * @author James
 */
public interface DrawableObject {
    public Location getLocation();
    public String getSpriteID();
    public Area getHitBox();
}
