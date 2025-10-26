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
    private List<Bullet> bullets;
    private Image backgroundImage;
    private int score;
    private int lives;
    private double rate;
    private boolean gameOver;
    private boolean gameWon;
    private int currentLevel;
    public int brickdestroyed=0;


    public GameManager() {
        paddle = new Paddle(350, 490, 150, 30);
        ball = new Ball(400, 300, 20, 20);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        bullets = new ArrayList<>();
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
    public int getBulletCount() {
        return paddle.getBulletCount();
    }

    public boolean isShooterActive() {
        return paddle.isShooterActive();
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
    private void resetPaddle(){
        paddle.setX(350);
    }

    public void handleMouseInput(double mouseX) {
        if (!gameOver && !gameWon) {
            paddle.setX(mouseX - paddle.getWidth() / 2);
        }
    }
    public void handleShoot() {
        bullets.addAll(paddle.shoot());
    }

    public void updateGame() {
        if (gameOver || gameWon) return;

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
                    brickdestroyed++;
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
                        gameOver = true;
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

        // 2. THÊM VÒNG LẶP CHO ĐẠN (BULLET)
        // (Đảm bảo bạn đã thêm 'List<Bullet> bullets;' và khởi tạo nó)
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();

            // Xóa đạn nếu ra khỏi màn hình
            if (bullet.isOutOfBounds()) {
                bulletIterator.remove();
                continue;
            }

            Iterator<Brick> bulletBrickIterator = bricks.iterator();
            while (bulletBrickIterator.hasNext()) {
                Brick brick = bulletBrickIterator.next();
                if (bullet.checkCollision(brick)) {
                    brick.takeHit(); // Gạch mất máu
                    bulletIterator.remove(); // Xóa viên đạn

                    if (brick.isDestroyed()) {
                        score += 10;
                        bulletBrickIterator.remove(); // <-- Sửa tên ở đây
                        brickdestroyed++;
                        // (Bạn có thể cho gạch bị đạn bắn vỡ cũng rơi ra PowerUp ở đây)
                    }
                    // Thoát vòng lặp gạch, vì đạn đã trúng 1 viên rồi
                    break;
                }
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

        if (currentLevel == 1 && brickdestroyed==9) {
            currentLevel++;
            brickdestroyed = 0;
            loadLevel(currentLevel);
        }else if (currentLevel == 2 && brickdestroyed==19) {
            currentLevel++;
            brickdestroyed = 0;
            loadLevel(currentLevel);
        }else if (currentLevel == 3&& brickdestroyed==56)  {
                gameWon = true;
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
    }

}