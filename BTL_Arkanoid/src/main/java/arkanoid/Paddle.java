package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double speed;
    private String currentPowerUp;
    private static final Image PADDLE_IMAGE = new Image(Paddle.class.getResource("/images/paddle.png").toString());

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 5.0;
        this.currentPowerUp = null;
    }

    public void moveLeft() { dx = -speed; }

    public void moveRight() { dx = speed; }

    public void stop() { dx = 0; }

    public void applyPowerUp(String powerUpType) {
        this.currentPowerUp = powerUpType;
        if (powerUpType.equals("ExpandPaddle")) {
            setWidth(getWidth() + 40);
        }
    }

    @Override
    public void move() {
        setX(getX() + dx);
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > 800) setX(800 - getWidth());
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(PADDLE_IMAGE, getX(), getY(), getWidth(), getHeight());
    }
}
