package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

// Lớp Ball đại diện cho quả bóng
public class Ball extends MovableObject {
    private double speed; // Tốc độ di chuyển của bóng
    private double directionX, directionY; // Hướng di chuyển theo trục x, y
    private static final Image BALL_IMAGE = new Image(Ball.class.getResource("/images/ball.png").toString()); // Hình ảnh của bóng

    // Constructor khởi tạo bóng
    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 1.0; // Tốc độ đã giảm từ trước
        this.directionX = 1.0;
        this.directionY = -1.0;
    }

    // Xử lý bóng nảy khi va chạm với đối tượng khác
    public void bounceOff(GameObject other) {
        if (other instanceof Paddle) {
            directionY = -Math.abs(directionY); // Nảy lên khi va chạm với thanh đỡ
            directionX = ((getX() + getWidth() / 2) - (other.getX() + other.getWidth() / 2)) / (other.getWidth() / 2); // Điều chỉnh góc nảy
        } else if (other instanceof Brick) {
            directionY = -directionY; // Nảy ngược lại khi va chạm với gạch
        }
    }

    // Kiểm tra va chạm với đối tượng khác
    public boolean checkCollision(GameObject other) {
        return getX() < other.getX() + other.getWidth() &&
                getX() + getWidth() > other.getX() &&
                getY() < other.getY() + other.getHeight() &&
                getY() + getHeight() > other.getY();
    }

    // Di chuyển bóng và xử lý va chạm với biên màn hình
    @Override
    public void move() {
        setX(getX() + dx);
        setY(getY() + dy);
        if (getX() <= 0 || getX() + getWidth() >= 800) directionX = -directionX; // Nảy ngang khi chạm biên trái/phải
        if (getY() <= 0) directionY = -directionY; // Nảy dọc khi chạm biên trên
    }

    // Cập nhật trạng thái bóng
    @Override
    public void update() {
        dx = speed * directionX;
        dy = speed * directionY;
        move();
    }

    // Vẽ bóng lên màn hình
    @Override
    public void render(GraphicsContext gc) {
        // gc.drawImage: Vẽ hình ảnh của bóng tại vị trí (x, y) với kích thước width x height
        if (BALL_IMAGE.isError()) {
            gc.setFill(Color.RED);
            gc.fillOval(getX(), getY(), getWidth(), getHeight()); // Vẽ hình tròn lớn hơn nếu không có hình ảnh
        } else {
            gc.drawImage(BALL_IMAGE, getX(), getY(), getWidth(), getHeight()); // Hình ảnh tự động co giãn theo kích thước
        }
    }
}
