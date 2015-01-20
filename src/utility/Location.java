package utility;

import utility.RNG;

public class Location {
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