package arkanoid;

import javafx.scene.canvas.GraphicsContext;

// Lớp trừu tượng cơ sở cho tất cả đối tượng trong game
public abstract class GameObject {
    private double x, y, width, height;

    // Constructor khởi tạo vị trí và kích thước đối tượng
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter và setter cho thuộc tính x, y, width, height
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }

    // Cập nhật trạng thái đối tượng (vị trí, tốc độ, v.v.)
    public abstract void update();

    // Vẽ đối tượng lên màn hình bằng GraphicsContext
    public abstract void render(GraphicsContext gc);
}
