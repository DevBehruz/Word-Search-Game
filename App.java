package org.openjfx.javafx_archetype_fxml;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;
import java.util.List;

public class App extends Application {

    private WordSearch wordSearch;
    private Grid grid;
    private char[][] gameGrid;
    private VBox remainingWordsBox;
    private Label celebrationLabel; // New celebration label

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Behruz' Word Search Game");

        // Initialize Grid and WordSearch
        grid = new Grid(20, 20);
        WordPlacer placer = new WordPlacer(grid);
        List<String> words = List.of("CROISSANT", "AMAZING", "SCONE", "FUDGE", "HOUSE");
        for (String word : words) {
            placer.placeWord(word);
        }
        grid.fillEmptySpaces();
        gameGrid = grid.getGridCopy();
        wordSearch = new WordSearch(grid, words);

        // Main Layout
        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(10));

        // Celebration Label
        celebrationLabel = new Label("All Words Found! 🎉");
        celebrationLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: green;");
        celebrationLabel.setVisible(false); // Hidden initially

        // Game Grid Display
        GridPane gridPane = new GridPane();
        updateGridDisplay(gridPane);

        // Remaining Words Display
        remainingWordsBox = new VBox(10);
        remainingWordsBox.setAlignment(Pos.TOP_LEFT);
        updateRemainingWordsDisplay();

        // Input Controls
        VBox inputLayout = createInputLayout(gridPane);

        // Combine layouts
        VBox centerLayout = new VBox(10, celebrationLabel, gridPane);
        centerLayout.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(centerLayout, remainingWordsBox, inputLayout);

        // Set up the scene
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the input layout for user interaction.
     */
    private VBox createInputLayout(GridPane gridPane) {
        VBox inputLayout = new VBox(10);
        inputLayout.setAlignment(Pos.CENTER);

        TextField wordInput = new TextField();
        wordInput.setPromptText("Enter word found");

        TextField xInput = new TextField();
        xInput.setPromptText("Enter x (row number)");

        TextField yInput = new TextField();
        yInput.setPromptText("Enter y (column letter)");

        ComboBox<String> orientationInput = new ComboBox<>();
        orientationInput.getItems().addAll("Horizontal", "Vertical", "Diagonal");
        orientationInput.setPromptText("Orientation");

        Button submitButton = new Button("Submit");

        Label resultLabel = new Label();

        submitButton.setOnAction(e -> {
            try {
                String word = wordInput.getText().toUpperCase();
                int x = Integer.parseInt(xInput.getText()); // Parse row number
                int y = yInput.getText().toLowerCase().charAt(0) - 'a'; // Convert column letter to index
                String orientation = orientationInput.getValue();
                int rowDelta = 0, colDelta = 0;

                // Determine orientation
                switch (orientation) {
                    case "Horizontal":
                        rowDelta = 0;
                        colDelta = 1;
                        break;
                    case "Vertical":
                        rowDelta = 1;
                        colDelta = 0;
                        break;
                    case "Diagonal":
                        rowDelta = 1;
                        colDelta = 1;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid orientation");
                }

                // Check and remove word
                if (wordSearch.findAndRemoveWord(word, x, y, rowDelta, colDelta)) {
                    resultLabel.setText("Word found: " + word);
                } else {
                    resultLabel.setText("Word not found");
                }

                // Refresh the grid and remaining words
                gameGrid = grid.getGridCopy();
                updateGridDisplay(gridPane);
                updateRemainingWordsDisplay();

                // Check if all words are found
                if (wordSearch.getRemainingWords().isEmpty()) {
                    showCelebration();
                }

                // Clear inputs
                wordInput.clear();
                xInput.clear();
                yInput.clear();
                orientationInput.getSelectionModel().clearSelection();

            } catch (Exception ex) {
                resultLabel.setText("Invalid input! Please try again.");
            }
        });

        inputLayout.getChildren().addAll(wordInput, xInput, yInput, orientationInput, submitButton, resultLabel);
        return inputLayout;
    }

    /**
     * Updates the display of the game grid in the GridPane with row and column labels.
     */
    private void updateGridDisplay(GridPane gridPane) {
        gridPane.getChildren().clear();

        // Add column headers (letters)
        for (int col = 0; col < grid.cols; col++) {
            Label columnHeader = new Label(String.valueOf((char) ('a' + col)));
            columnHeader.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            gridPane.add(columnHeader, col + 1, 0); // Offset by 1 for row labels
        }

        // Add row headers (numbers) and grid cells
        for (int row = 0; row < grid.rows; row++) {
            Label rowHeader = new Label(String.format("%02d", row));
            rowHeader.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            gridPane.add(rowHeader, 0, row + 1); // Offset by 1 for column labels

            for (int col = 0; col < grid.cols; col++) {
                Label cell = new Label(String.valueOf(gameGrid[row][col]));
                cell.setMinSize(30, 30);

                if (gameGrid[row][col] == '*') {
                    cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 14; -fx-background-color: yellow;");
                } else {
                    cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 14;");
                }

                gridPane.add(cell, col + 1, row + 1);
            }
        }
    }

    /**
     * Updates the remaining words display.
     */
    private void updateRemainingWordsDisplay() {
        remainingWordsBox.getChildren().clear();
        remainingWordsBox.getChildren().add(new Label("Remaining Words:"));
        for (String word : wordSearch.getRemainingWords()) {
            remainingWordsBox.getChildren().add(new Label("- " + word));
        }
    }

    /**
     * Shows the celebration label with scaling animation.
     */
    private void showCelebration() {
        celebrationLabel.setVisible(true);

        // Add scaling animation
        ScaleTransition scale = new ScaleTransition(Duration.seconds(1), celebrationLabel);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setCycleCount(3);
        scale.setAutoReverse(true);
        scale.play();
    }
}

