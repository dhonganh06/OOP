package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    public enum GameState {
        PLAYING,
        TRANSITION,
        GAME_OVER,
        GAME_WON
    }
    private GameState currentState;
    private long transitionStartTime;
    private static final long TRANSITION_DELAY_NANOS = 500 * 1_000_000;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private List<Bullet> bullets;
    private Image backgroundImage;
    private int score;
    private int lives;
    private double rate;
    private int currentLevel;

    public GameManager() {
        paddle = new Paddle(350, 490, 150, 30);
        ball = new Ball(400, 300, 20, 20);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        bullets = new ArrayList<>();
        score = 0;
        lives = 3;
        currentState = GameState.PLAYING;
        currentLevel = 1;

        try {
            backgroundImage = new Image(GameManager.class.getResource("/images/background.jpg").toString());
        } catch (Exception e) {
            System.out.println("Lỗi không tải");
            backgroundImage = null;
        }

        loadLevel(currentLevel);
    }
    public int getBulletCount() {
        return paddle.getBulletCount();
    }

    public boolean isShooterActive() {
        return paddle.isShooterActive();
    }

    public int getScore() { return this.score; }
    public int getLives() { return this.lives; }
    public int getCurrentLevel() { return this.currentLevel; }

    public GameState getCurrentState() {
        return this.currentState;
    }

    private void loadLevel(int levelNumber) {
        bricks.clear();
        powerUps.clear();
        bullets.clear();
        String levelFile = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = GameManager.class.getResourceAsStream(levelFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) {
                currentState = GameState.GAME_WON;
                return;
            }
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char blockType = line.charAt(col);
                    if (blockType == '1') {
                        bricks.add(new Brick(col * 70 + 10, row * 20 + 50, 70, 20, "Normal", 1));
                    } else if (blockType == '2') {
                        bricks.add(new Brick(col * 70 + 10, row * 20 + 50, 70, 20, "Strong", 2));
                    }
                }
                row++;
            }
        } catch (Exception e) {
            currentState = GameState.GAME_WON;
            e.printStackTrace();
        }
        resetPaddleAndBall();
    }

    private void resetPaddleAndBall() {
        ball.setX(400);
        ball.setY(500);
        paddle.setX(350);
    }
    private void resetPaddle(){
        paddle.setX(350);
    }

    public void handleMouseInput(double mouseX) {
        if (currentState == GameState.PLAYING) {
            paddle.setX(mouseX - paddle.getWidth() / 2);
        }
    }
    public void handleShoot() {
       if(currentState == GameState.PLAYING){
           bullets.addAll(paddle.shoot());
       }
    }

    public void updateGame() {
        if (currentState == GameState.GAME_OVER || currentState == GameState.GAME_WON) return;
        if (currentState == GameState.TRANSITION) {
            if(System.nanoTime() - transitionStartTime > TRANSITION_DELAY_NANOS) {
                currentState = GameState.PLAYING;
                loadLevel(currentLevel);
            }
            return;
        }
        paddle.update();
        ball.update();

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        Iterator<Brick> ballBrickIterator = bricks.iterator();
        while (ballBrickIterator.hasNext()) {
            Brick brick = ballBrickIterator.next();
            if (ball.checkCollision(brick)) {
                brick.takeHit();
                ball.bounceOff(brick);
                if (brick.isDestroyed()) {
                    score += 10;
                    ballBrickIterator.remove();
                    rate = Math.random();
                    if (rate <=  0.2) {
                        powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                    }
                    if (rate <= 0.4&& rate >0.2) {
                        powerUps.add(new Shooterpowerup(brick.getX(), brick.getY(), 20, 20));
                    }
                    if (rate <= 0.5&& rate > 0.4) {
                        powerUps.add(new Bomb(brick.getX(), brick.getY(), 20, 20));
                    }

                }
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            powerUp.update();
            if (paddle.checkCollision(powerUp)) {
                if (powerUp.type.equals("Bomb")) {
                    lives--;
                    score -= 50;
                    if(lives == 0){
                        currentState =  GameState.GAME_OVER;
                    }
                    if (score < 0) {
                        score = 0;
                    }
                    resetPaddle();
                } else {
                    boolean effectApplied = powerUp.applyEffect(paddle);
                    if (effectApplied) {
                        Sound.getInstance().playSound("powerup");
                    } else {
                        score += 100;
                    }
                }

                powerUpIterator.remove();

            } else if (powerUp.getY() > 600) {
                powerUpIterator.remove();
            }
        }

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();

            if (bullet.isOutOfBounds()) {
                bulletIterator.remove();
                continue;
            }

            Iterator<Brick> bulletBrickIterator = bricks.iterator();
            while (bulletBrickIterator.hasNext()) {
                Brick brick = bulletBrickIterator.next();
                if (bullet.checkCollision(brick)) {
                    brick.takeHit();
                    bulletIterator.remove();

                    if (brick.isDestroyed()) {
                        score += 10;
                        bulletBrickIterator.remove();
                    }
                    break;
                }
            }
        }

        if (ball.getY() > 600) {
            lives--;
            if (lives <= 0) {
                currentState = GameState.GAME_OVER;
            } else {
                resetPaddleAndBall();
            }
        }

        if (bricks.isEmpty()) {
            if (currentLevel == 3) {
                currentState = GameState.GAME_WON;
            } else {
                currentState = GameState.TRANSITION;
                transitionStartTime = System.nanoTime();
                currentLevel++;
            }
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
        for (Bullet bullet : bullets) {
            bullet.render(gc);
        }
        if (currentState == GameState.TRANSITION) {
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Verdana", 50));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("Level " + currentLevel, 400, 270);
        }
    }

}