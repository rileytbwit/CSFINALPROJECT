package Lab3ModuleInfo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Lab3 extends Application {

    private HangmanGame game;
    private Label wordLabel = new Label();
    private TextField guessField = new TextField();
    private Button guessButton = new Button("Guess");
    private Button restartButton = new Button("Restart");
    private Label statusLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        game = new HangmanGame("secret", 5); 
        wordLabel.setText(game.getDisplayedWord());

        VBox root = new VBox(10); 
        HBox buttonContainer = new HBox(10); 

        buttonContainer.getChildren().addAll(guessButton, restartButton); 

        root.getChildren().addAll(wordLabel, guessField, buttonContainer, statusLabel); 
        guessButton.setOnAction(e -> handleGuess());
        restartButton.setOnAction(e -> restartGame());

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("hangman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleGuess() {
        String guess = guessField.getText().trim().toLowerCase();
        if (guess.isEmpty() || guess.length() != 1) {
            statusLabel.setText("Please enter a single letter.");
            return;
        }

        if (game.guess(guess.charAt(0))) {
            wordLabel.setText(game.getDisplayedWord());
            statusLabel.setText("Good guess! Remaining guesses: " + game.getRemainingGuesses());
        } else {
            statusLabel.setText("Wrong guess! Try again. Remaining guesses: " + game.getRemainingGuesses());
        }

        guessField.clear();

        if (game.isWon()) {
            statusLabel.setText("Congratulations! You've guessed the word!");
            guessButton.setDisable(true);
        } else if (game.isGameOver()) {
            statusLabel.setText("Game over! The word was: " + game.getSecretWord());
            guessButton.setDisable(true);
        }
    }

    private void restartGame() {
        game = new HangmanGame("secret",5); // reset
        wordLabel.setText(game.getDisplayedWord());
        statusLabel.setText("");
        guessButton.setDisable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class HangmanGame {
    private String secretWord;
    private StringBuilder displayedWord;
    private int wrongGuesses;
    private int maxGuesses;

    public HangmanGame(String word, int maxGuesses) {
        this.secretWord = word;
        this.maxGuesses = maxGuesses;
        this.displayedWord = new StringBuilder("_".repeat(word.length()));
        this.wrongGuesses = 0;
    }

    public boolean guess(char letter) {
        boolean isCorrect = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                displayedWord.setCharAt(i, letter);
                isCorrect = true;
            }
        }

        if (!isCorrect) {
            wrongGuesses++;
        }

        return isCorrect;
    }

    public boolean isWon() {
        return displayedWord.toString().equals(secretWord);
    }

    public boolean isGameOver() {
        return wrongGuesses >= maxGuesses;
    }

    public String getDisplayedWord() {
        return displayedWord.toString();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public int getRemainingGuesses() {
        return maxGuesses - wrongGuesses;
    }
}