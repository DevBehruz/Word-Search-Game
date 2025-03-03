package org.openjfx.javafx_archetype_fxml;

import java.util.Arrays;
import java.util.Random;

class Grid {
    final int rows;
    final int cols;
    private final char[][] grid;
    private static final Random random = new Random();
    
    Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], '.');
        }
    }
    
    boolean canPlaceWord(String word, int row, int col, int rowDelta, int colDelta) {
        if (row < 0 || col < 0) return false;
        
        for (int i = 0; i < word.length(); i++) {
            int newRow = row + (i * rowDelta);
            int newCol = col + (i * colDelta);
            
            // Check if the position is out of bounds
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return false;
            }
            
            char currentChar = grid[newRow][newCol];
            if (currentChar != '.' && currentChar != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    void placeWord(String word, int row, int col, int rowDelta, int colDelta) {
        for (int i = 0; i < word.length(); i++) {
            grid[row + (i * rowDelta)][col + (i * colDelta)] = word.charAt(i);
        }
    }
    
    void fillEmptySpaces() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    grid[i][j] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }
    
    char[][] getGridCopy() {
        // Method to get a copy of the grid
        char[][] copy = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, cols);
        }
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j]);
                if (j < cols - 1) {
                    sb.append(' ');
                }
            }
            if (i < rows - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
