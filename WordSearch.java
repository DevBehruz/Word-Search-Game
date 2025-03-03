package org.openjfx.javafx_archetype_fxml;

import java.util.ArrayList;
import java.util.List;

/**
 * Word Search Game: Backend Logic
 */
public class WordSearch {
    private final Grid grid;
    private final List<String> remainingWords;
    private final char[][] gameGrid;

    public WordSearch(Grid grid, List<String> words) {
        this.grid = grid;
        this.remainingWords = new ArrayList<>(words);
        this.gameGrid = grid.getGridCopy();
    }

    public boolean findAndRemoveWord(String word, int startRow, int startCol, int rowDelta, int colDelta) {
        word = word.toUpperCase();

        // Directly check the word path from the given coordinates
        if (checkWordPath(word, startRow, startCol, rowDelta, colDelta)) {
            remainingWords.remove(word);
            return true;
        }
        return false;
    }

    private boolean checkWordPath(String word, int startRow, int startCol, int rowDelta, int colDelta) {
        // Check if the entire word can be found from this starting point
        for (int i = 0; i < word.length(); i++) {
            int currentRow = startRow + (i * rowDelta);
            int currentCol = startCol + (i * colDelta);

            // Check boundaries
            if (currentRow < 0 || currentRow >= grid.rows ||
                currentCol < 0 || currentCol >= grid.cols) {
                return false;
            }

            // Check if character matches
            if (gameGrid[currentRow][currentCol] != word.charAt(i)) {
                return false;
            }
        }

        // Replace letters with '*' in both gameGrid and grid
        for (int i = 0; i < word.length(); i++) {
            int currentRow = startRow + (i * rowDelta);
            int currentCol = startCol + (i * colDelta);
            gameGrid[currentRow][currentCol] = '*';
        }
        grid.placeWord("*".repeat(word.length()), startRow, startCol, rowDelta, colDelta);

        return true;
    }

    public char[][] getGameGrid() {
        return gameGrid;
    }

    public List<String> getRemainingWords() {
        return remainingWords;
    }

    public void printGrid() {
        // Print column headers
        System.out.print("   ");
        for (char c = 'a'; c < 'a' + grid.cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        // Print row numbers and grid
        for (int i = 0; i < grid.rows; i++) {
            // Print row number with two digits
            System.out.printf("%02d ", i);

            for (int j = 0; j < grid.cols; j++) {
                System.out.print(gameGrid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
