package arkanoid;

// Lớp trừu tượng cho các đối tượng có thể di chuyển
public abstract class MovableObject extends GameObject {
    protected double dx, dy; // Tốc độ di chuyển theo trục x, y

    // Constructor khởi tạo vị trí, kích thước và tốc độ
    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    // Di chuyển đối tượng dựa trên tốc độ dx, dy
    public abstract void move();
}
