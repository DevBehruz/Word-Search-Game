package org.openjfx.javafx_archetype_fxml;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

class WordPlacer {
    private final Grid grid;
    private final Random random = new Random();
    private static final int[][] DIRECTIONS = {
        {0, 1},   // horizontal
        {1, 0},   // vertical
        {1, 1}    // diagonal down-right
    };
    
    private final Set<Integer> usedDirections = new HashSet<>();
    
    // Tracks the most recently used direction
    private int lastDirection = -1;
    
    WordPlacer(Grid grid) {
        this.grid = grid;
    }
    
    boolean placeWord(String word) {
        word = word.toUpperCase();
        for (int i = 0; i < DIRECTIONS.length; i++) {
            if (!usedDirections.contains(i)) {
                if (tryPlaceWord(word, i)) {
                    usedDirections.add(i);
                    lastDirection = i;
                    return true;
                }
            }
        }
        for (int attempts = 0; attempts < 100; attempts++) {
            int directionIndex = random.nextInt(DIRECTIONS.length);
            // Avoid using same direction twice in a row if possible
            if (directionIndex != lastDirection && tryPlaceWord(word, directionIndex)) {
                usedDirections.add(directionIndex);
                lastDirection = directionIndex;
                return true;
            }
        }
        for (int attempts = 0; attempts < 50; attempts++) {
            int directionIndex = random.nextInt(DIRECTIONS.length);
            if (tryPlaceWord(word, directionIndex)) {
                usedDirections.add(directionIndex);
                lastDirection = directionIndex;
                return true;
            }
        }
        for (int[] direction : DIRECTIONS) {
            int directionIndex = getDirectionIndex(direction);
            for (int row = 0; row < grid.rows; row++) {
                for (int col = 0; col < grid.cols; col++) {
                    if (grid.canPlaceWord(word, row, col, direction[0], direction[1])) {
                        grid.placeWord(word, row, col, direction[0], direction[1]);
                        usedDirections.add(directionIndex);
                        lastDirection = directionIndex;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int getDirectionIndex(int[] direction) {
        for (int i = 0; i < DIRECTIONS.length; i++) {
            if (DIRECTIONS[i][0] == direction[0] && DIRECTIONS[i][1] == direction[1]) {
                return i;
            }
        }
        return 0;
    }
    
    private boolean tryPlaceWord(String word, int directionIndex) {
        int rowDelta = DIRECTIONS[directionIndex][0];
        int colDelta = DIRECTIONS[directionIndex][1];
        
        int maxRow = grid.rows - (word.length() * Math.abs(rowDelta));
        int maxCol = grid.cols - (word.length() * Math.abs(colDelta));
        
        if (maxRow < 0 || maxCol < 0) return false;
        
        // Increase attempts for better placement chances
        for (int attempt = 0; attempt < 30; attempt++) {
            int row = random.nextInt(maxRow + 1);
            int col = random.nextInt(maxCol + 1);
            
            if (grid.canPlaceWord(word, row, col, rowDelta, colDelta)) {
                grid.placeWord(word, row, col, rowDelta, colDelta);
                return true;
            }
        }
        
        return false;
    }
}
