package org.openjfx.javafx_archetype_fxml;

import java.util.List;

class GameData {
    final int rows;
    final int cols;
    final List<String> words;
    
    // Constructor to initialize the game data
    GameData(int rows, int cols, List<String> words) {
        this.rows = rows;
        this.cols = cols;
        this.words = words;
    }
}
