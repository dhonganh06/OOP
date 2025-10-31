package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends MovableObject {

    private double speed = 6.0;

    public Bullet(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.dy = -speed;
    }

    @Override
    public void move() {
        setY(getY() + dy);
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public boolean isOutOfBounds() {
        return getY() + getHeight() < 0;
    }
}