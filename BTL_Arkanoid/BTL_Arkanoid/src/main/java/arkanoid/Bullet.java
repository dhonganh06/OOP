package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends MovableObject {

    private double speed = 6.0; // Tốc độ của đạn

    public Bullet(double x, double y, double width, double height) {
        super(x, y, width, height);
        // dy âm để di chuyển lên trên
        this.dy = -speed;
    }

    @Override
    public void move() {
        // Di chuyển đạn theo trục Y
        setY(getY() + dy);
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(GraphicsContext gc) {
        // Vẽ viên đạn (ví dụ là một hình chữ nhật màu vàng)
        // Bạn có thể thay bằng ảnh nếu muốn
        gc.setFill(Color.YELLOW);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Kiểm tra xem đạn đã bay ra khỏi màn hình chưa
     */
    public boolean isOutOfBounds() {
        // Khi đạn bay qua đỉnh (y < 0)
        return getY() + getHeight() < 0;
    }
}