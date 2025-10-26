package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private Image backgroundImage;
    private int score;
    private int lives;
    private boolean gameOver;
    private boolean gameWon;
    private int currentLevel;

    public GameManager() {
        paddle = new Paddle(350, 490, 150, 30);
        ball = new Ball(400, 300, 20, 20);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameOver = false;
        gameWon = false;
        currentLevel = 1;

        try {
            backgroundImage = new Image(GameManager.class.getResource("/images/background.jpg").toString());
        } catch (Exception e) {
            System.out.println("Lỗi không tải");
            backgroundImage = null;
        }

        loadLevel(currentLevel);
    }

    public int getScore() { return this.score; }
    public int getLives() { return this.lives; }
    public int getCurrentLevel() { return this.currentLevel; }
    public boolean isGameOver() { return this.gameOver; }
    public boolean isGameWon() { return this.gameWon; }

    private void loadLevel(int levelNumber) {
        bricks.clear();
        powerUps.clear();
        String levelFile = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = GameManager.class.getResourceAsStream(levelFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) {
                gameWon = true;
                return;
            }
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char blockType = line.charAt(col);
                    if (blockType == '1') {
                        bricks.add(new Brick(col * 80 + 10, row * 30 + 50, 70, 20, "Normal", 1));
                    } else if (blockType == '2') {
                        bricks.add(new Brick(col * 80 + 10, row * 30 + 50, 70, 20, "Strong", 2));
                    }
                }
                row++;
            }
        } catch (Exception e) {
            gameWon = true;
            e.printStackTrace();
        }
        resetPaddleAndBall();
    }

    private void resetPaddleAndBall() {
        ball.setX(400);
        ball.setY(500);
        paddle.setX(350);
    }

    public void handleMouseInput(double mouseX) {
        if (!gameOver && !gameWon) {
            paddle.setX(mouseX - paddle.getWidth() / 2);
        }
    }

    public void updateGame() {
        if (gameOver || gameWon) return;

        paddle.update();
        ball.update();

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.checkCollision(brick)) {
                brick.takeHit();
                ball.bounceOff(brick);
                if (brick.isDestroyed()) {
                    score += 10;
                    brickIterator.remove();
                    if (Math.random() < 0.2) {
                        powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                    }
                }
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            powerUp.update();
            if (paddle.checkCollision(powerUp)) {
                powerUp.applyEffect(paddle);
                powerUpIterator.remove();
            } else if (powerUp.getY() > 600) {
                powerUpIterator.remove();
            }
        }

        if (ball.getY() > 600) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            } else {
                resetPaddleAndBall();
            }
        }

        if (currentLevel == 1 && score >= 90) {
            currentLevel++;
            loadLevel(currentLevel);
        } else if (currentLevel == 2 && score >= 250) {
            currentLevel++;
            loadLevel(currentLevel);
        }
    }

    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, 800, 540);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, 800, 540);
        }

        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.render(gc);
        }

    }
}