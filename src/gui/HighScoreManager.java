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
package gui;

import functionalInterfaces.TriConsumer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author James Elderfield
 */
public class HighScoreManager {
    public HighScoreManager(String fileName) {
        updateFromFile(fileName);
    }
    
    public void updateFromFile(String fileName) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            
            json = new JSONObject(new String(bytes));
        } catch(IOException e) {
            // Do nowt
        }
    }
    
    public class ScorePair {
        public ScorePair(String playerName, double score) {
            this.playerName = playerName;
            this.score = score;
        }
        
        public String playerName;
        public double score;
    }
    
    public void addScore(ScorePair newScore) {
        json.append(newScore.playerName, newScore.score);
    }
    
    public void saveScoresToFile(String fileName) {
        try {
            PrintWriter write = new PrintWriter(fileName);
            
            write.print(json.toString());
            
            write.close();
        } catch(FileNotFoundException e) {
            // Surely this can't happen?
        }
    }
    
    public List<ScorePair> getTopNScores(int n) {
        List<ScorePair> retList = new ArrayList<>();
        
        // Maintain the list in descending score
        TriConsumer<List<ScorePair>, ScorePair, Integer> insertScore = (List<ScorePair> existing, ScorePair newScore, Integer maxSize) -> {
            int i;
            
            for(i = 0; i < existing.size(); i++) {
                if(existing.get(i).score < newScore.score) {
                    break;
                }
            }
            
            existing.add(i, newScore);
            
            if(existing.size() > maxSize) {
                existing.remove(existing.size() - 1);
            }
        };
        
        JSONArray names = json.names();
        json.keys();
        
        for(int i = 0; names != null && i < names.length(); i++) {
            String key = (String)names.get(i);
            
            JSONArray allScores = json.getJSONArray(key);
            
            for(int j = 0; j < allScores.length(); j++) {
                ScorePair testPair = new ScorePair(key, allScores.getDouble(j));
                insertScore.accept(retList, testPair, n);
            }
        }
        
        return retList;
    }
    
    JSONObject json = new JSONObject();
}
