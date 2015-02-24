/* 
 * The MIT License
 *
 * Copyright 2015 James.
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
package utility;

public class Location {
    // Produces location at set location
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Produces a random location within limits
    public Location(Location upperLeft, int height, int width, RNG rng) {
        this.x = upperLeft.getX() + rng.uniformIntInRange(0, width);
        this.y = upperLeft.getY() + rng.uniformIntInRange(0, height);
    }
    
    public void setX(int newValue) {
        x = newValue;
    }
    
    public void setY(int newValue) {
        y = newValue;
    }
    
    public boolean equals(Location other) {
        return x == other.x && y == other.y;
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