package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Lớp ExpandPaddlePowerUp để tăng kích thước thanh đỡ
public class ExpandPaddlePowerUp extends PowerUp {
    private static final Image POWERUP_IMAGE = new Image(ExpandPaddlePowerUp.class.getResource("/images/powerup_expand.png").toString()); //Hình ảnh item tăng kích thước
    // Constructor khởi tạo power-up
    public ExpandPaddlePowerUp(double x, double y, double width, double height) {
        super(x, y, width, height, "ExpandPaddle", 10.0);
    }

    // Áp dụng hiệu ứng tăng kích thước thanh đỡ
    @Override
    public void applyEffect(Paddle paddle) {
        paddle.applyPowerUp(type);
    }

    // Xóa hiệu ứng, trả kích thước thanh đỡ về ban đầu
    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setWidth(paddle.getWidth() / 1.5);
    }

    // Vẽ power-up lên màn hình
    @Override
    public void render(GraphicsContext gc) {
        // gc.drawImage: Vẽ hình ảnh power-up tại vị trí (x, y) với kích thước width x height
        gc.drawImage(POWERUP_IMAGE, getX(), getY(), getWidth(), getHeight());
    }
}
