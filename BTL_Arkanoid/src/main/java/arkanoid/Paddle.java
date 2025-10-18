package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Lớp Paddle đại diện cho thanh đỡ do người chơi điều khiển
public class Paddle extends MovableObject {
    private double speed; // Tốc độ di chuyển của thanh đỡ
    private String currentPowerUp; // Loại power-up đang áp dụng
    private static final Image PADDLE_IMAGE = new Image(Paddle.class.getResource("/images/paddle.png").toString()); // Tải hình ảnh từ resources

    // Constructor khởi tạo thanh đỡ
    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 5.0;
        this.currentPowerUp = null;
    }

    // Di chuyển thanh đỡ sang trái
    public void moveLeft() { dx = -speed; }

    // Di chuyển thanh đỡ sang phải
    public void moveRight() { dx = speed; }

    // Dừng di chuyển thanh đỡ
    public void stop() { dx = 0; }

    // Áp dụng hiệu ứng power-up (ví dụ: tăng kích thước)
    public void applyPowerUp(String powerUpType) {
        this.currentPowerUp = powerUpType;
        if (powerUpType.equals("ExpandPaddle")) {
            setWidth(getWidth() * 1.5); // Tăng kích thước thanh đỡ
        }
    }

    // Di chuyển thanh đỡ và giới hạn trong màn hình
    @Override
    public void move() {
        setX(getX() + dx);
        if (getX() < 0) setX(0); // Không cho vượt ra ngoài mép trái
        if (getX() + getWidth() > 800) setX(800 - getWidth()); // Không cho vượt ra ngoài mép phải
    }

    // Cập nhật trạng thái thanh đỡ
    @Override
    public void update() {
        move();
    }

    // Vẽ thanh đỡ lên màn hình
    @Override
    public void render(GraphicsContext gc) {
        // gc.drawImage: Vẽ hình ảnh của thanh đỡ tại vị trí (x, y) với kích thước width x height
        gc.drawImage(PADDLE_IMAGE, getX(), getY(), getWidth(), getHeight());
    }
}
