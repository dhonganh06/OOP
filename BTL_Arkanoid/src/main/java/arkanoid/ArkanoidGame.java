package arkanoid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ArkanoidGame extends Application {

    private Stage primaryStage;
    private GameManager gameManager;
    private AnimationTimer gameTimer;

    private Label scoreLabel;
    private Label levelLabel;
    private Label livesLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Arkanoid Game");

        showStartMenu();

        primaryStage.show();
    }

    private void showStartMenu() {
        Image backgroundImage = new Image(ArkanoidGame.class.getResource("/images/menu_background.jpg").toString());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 22px; -fx-padding: 12 25; -fx-background-radius: 10;");

        Button instructionButton = new Button("Instructions");
        instructionButton.setStyle("-fx-font-size: 22px; -fx-padding: 12 25; -fx-background-radius: 10;");

        startButton.setOnAction(e -> startGame());
        instructionButton.setOnAction(e -> showInstructions());

        VBox buttonLayout = new VBox(25);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(startButton, instructionButton);

        StackPane menuLayout = new StackPane();
        menuLayout.getChildren().addAll(backgroundImageView, buttonLayout);

        Scene menuScene = new Scene(menuLayout, 800, 600);
        primaryStage.setScene(menuScene);
    }

    private void startGame() {
        BorderPane root = new BorderPane();

        HBox uiBar = new HBox(30);
        uiBar.setAlignment(Pos.CENTER);
        uiBar.setPadding(new Insets(10));
        uiBar.setStyle("-fx-background-color: #2c3e50;");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        levelLabel = new Label("Level: 1");
        levelLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        livesLabel = new Label("Lives: 3");
        livesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        uiBar.getChildren().addAll(scoreLabel, levelLabel, livesLabel);

        Canvas canvas = new Canvas(800, 540);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setTop(uiBar);
        root.setCenter(canvas);
        gameManager = new GameManager();
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameManager.updateGame();
                gameManager.render(gc);

                // Cập nhật text trên các Label của thanh UI
                scoreLabel.setText("Score: " + gameManager.getScore());
                levelLabel.setText("Level: " + gameManager.getCurrentLevel());
                livesLabel.setText("Lives: " + gameManager.getLives());
                if (gameManager.isGameOver()) {
                    gameTimer.stop();
                    showEndGameScreen(false, gameManager.getScore());
                } else if (gameManager.isGameWon()) {
                    gameTimer.stop();
                    showEndGameScreen(true, gameManager.getScore());
                }
            }
        };

        // 6. Tạo Scene và hiển thị
        Scene gameScene = new Scene(root);
        gameScene.setOnMouseMoved(e -> gameManager.handleMouseInput(e.getX()));

        primaryStage.setScene(gameScene);
        gameTimer.start();
    }

    private void showInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("Cách chơi Arkanoid");
        alert.setContentText(
                "- Dùng chuột để di chuyển thanh đỡ (Paddle).\n" +
                        "- Đừng để bóng (Ball) rơi xuống dưới màn hình.\n" +
                        "- Đạt đủ điểm yêu cầu để qua màn.\n"
        );
        alert.showAndWait();
    }

    private void showEndGameScreen(boolean wonGame, int finalScore) {
        String imagePath;
        String message;
        Color messageColor;

        if (wonGame) {
            imagePath = "/images/you_win_background.jpg";
            message = "YOU WIN!\nFinal Score: " + finalScore;
            messageColor = Color.LIMEGREEN;
        } else {
            imagePath = "/images/game_over_background.jpg";
            message = "GAME OVER\nFinal Score: " + finalScore;
            messageColor = Color.RED;
        }

        Image backgroundImage = new Image(ArkanoidGame.class.getResource(imagePath).toString());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);

        Text endGameText = new Text(message);
        endGameText.setFont(Font.font("Verdana", 50));
        endGameText.setFill(messageColor);
        endGameText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER); // Căn giữa text

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setStyle("-fx-font-size: 20px; -fx-padding: 10 20; -fx-background-radius: 10;");
        playAgainButton.setOnAction(e -> {
            gameManager = null;
            gameTimer = null;
            showStartMenu();
        });

        // Tạo nút "Exit"
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10 20; -fx-background-radius: 10;");
        exitButton.setOnAction(e -> primaryStage.close()); // Đóng ứng dụng
        VBox buttonLayout = new VBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(endGameText, playAgainButton, exitButton);

        StackPane endScreenLayout = new StackPane();
        endScreenLayout.getChildren().addAll(backgroundImageView, buttonLayout);

        Scene endScreenScene = new Scene(endScreenLayout, 800, 600);
        primaryStage.setScene(endScreenScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}