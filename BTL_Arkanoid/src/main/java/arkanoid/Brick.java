package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Lớp Brick đại diện cho gạch cần phá
public class Brick extends GameObject {
    private int hitPoints; // Số lần va chạm để phá hủy
    private String type; // Loại gạch (Normal hoặc Strong)
    private static final Image NORMAL_BRICK_IMAGE = new Image(Brick.class.getResource("/images/brick_normal.png").toString());
    private static final Image STRONG_BRICK_IMAGE = new Image(Brick.class.getResource("/images/brick_strong.png").toString());

    // Constructor khởi tạo gạch
    public Brick(double x, double y, double width, double height, String type, int hitPoints) {
        super(x, y, width, height);
        this.type = type;
        this.hitPoints = hitPoints;
    }

    // Giảm số lần va chạm khi bị bóng chạm
    public void takeHit() {
        hitPoints--;
    }

    // Kiểm tra xem gạch đã bị phá hủy chưa
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    // Không cập nhật trạng thái vì gạch không di chuyển
    @Override
    public void update() {}

    // Vẽ gạch lên màn hình
    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            // Chọn hình ảnh dựa trên loại gạch
            Image brickImage = type.equals("Normal") ? NORMAL_BRICK_IMAGE : STRONG_BRICK_IMAGE;
            // gc.drawImage: Vẽ hình ảnh của gạch tại vị trí (x, y) với kích thước width x height
            gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());
        }
    }
}
