package arkanoid;

public abstract class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public abstract void move();
}
