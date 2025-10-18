package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

// Lớp GameManager quản lý logic game
public class GameManager {
    private Paddle paddle; // Thanh đỡ
    private Ball ball; // Quả bóng
    private List<Brick> bricks; // Danh sách gạch
    private List<PowerUp> powerUps; // Danh sách power-up
    private int score; // Điểm số
    private int lives; // Số mạng
    private boolean gameOver; // Trạng thái game kết thúc

    // Constructor khởi tạo game
    public GameManager() {
        paddle = new Paddle(350, 550, 100, 10);
        ball = new Ball(400, 500, 200, 200); // Tăng kích thước bóng từ 10x10 lên 200x200
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameOver = false;

        // Khởi tạo lưới gạch
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks.add(new Brick(j * 80 + 10, i * 30 + 50, 70, 20, i % 2 == 0 ? "Normal" : "Strong", i % 2 == 0 ? 1 : 2));
            }
        }
    }

    // Xử lý đầu vào từ bàn phím
    public void handleInput(KeyCode code, boolean isPressed) {
        if (isPressed) {
            if (code == KeyCode.LEFT) paddle.moveLeft();
            if (code == KeyCode.RIGHT) paddle.moveRight();
        } else {
            paddle.stop();
        }
    }

    // Cập nhật trạng thái game
    public void updateGame() {
        if (gameOver) return;

        paddle.update();
        ball.update();

        // Kiểm tra va chạm với thanh đỡ
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        // Kiểm tra va chạm với gạch
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.checkCollision(brick) && !brick.isDestroyed()) {
                brick.takeHit();
                ball.bounceOff(brick);
                score += 10;
                if (brick.isDestroyed() && Math.random() < 0.2) {
                    powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                }
            }
        }

        // Kiểm tra va chạm với power-up
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            powerUp.update();
            if (ball.checkCollision(powerUp)) {
                powerUp.applyEffect(paddle);
                powerUpIterator.remove();
            }
            if (powerUp.getY() > 600) powerUpIterator.remove();
        }

        // Kiểm tra bóng ra ngoài màn hình
        if (ball.getY() > 600) {
            lives--;
            ball.setX(400);
            ball.setY(500);
            ball.update();
            if (lives <= 0) gameOver = true;
        }
    }

    // Vẽ toàn bộ game lên màn hình
    public void render(GraphicsContext gc) {
        // gc.setFill và gc.fillRect: Tô nền trắng cho toàn bộ màn hình
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 800, 600);
        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) brick.render(gc);
        for (PowerUp powerUp : powerUps) powerUp.render(gc);

        // gc.setFill và gc.fillText: Hiển thị điểm số và số mạng bằng màu đen để tương phản với nền trắng
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Lives: " + lives, 700, 20);
        if (gameOver) gc.fillText("Game Over", 350, 300);
    }
}
