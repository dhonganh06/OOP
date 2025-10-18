package arkanoid;

import javafx.scene.canvas.GraphicsContext;

// Lớp trừu tượng PowerUp cho các vật phẩm tăng sức mạnh
public abstract class PowerUp extends GameObject {
    protected String type; // Loại power-up
    protected double duration; // Thời gian hiệu lực của power-up

    // Constructor khởi tạo power-up
    public PowerUp(double x, double y, double width, double height, String type, double duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    // Áp dụng hiệu ứng power-up lên thanh đỡ
    public abstract void applyEffect(Paddle paddle);

    // Xóa hiệu ứng power-up khỏi thanh đỡ
    public abstract void removeEffect(Paddle paddle);

    // Cập nhật vị trí power-up (rơi xuống dưới)
    @Override
    public void update() {
        setY(getY() + 2);
    }

    // Phương thức render mặc định, sẽ được ghi đè
    @Override
    public void render(GraphicsContext gc) {}
}
