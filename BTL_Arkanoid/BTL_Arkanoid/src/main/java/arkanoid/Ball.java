package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double speed;
    private double directionX, directionY;
    private static final Image BALL_IMAGE = new Image(Ball.class.getResource("/images/ball.png").toString());


    public Ball(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 3.0;
        this.directionX = 1.0;
        this.directionY = -1.0;
    }

    public void bounceOff(GameObject other) {
        if (other instanceof Paddle) {
            directionY = -Math.abs(directionY);
            directionX = ((getX() + getWidth() / 2) - (other.getX() + other.getWidth() / 2)) / (other.getWidth() / 2);
        } else if (other instanceof Brick) {
            directionY = -directionY;
        }
        Sound.getInstance().playSound("bounce");
    }

    public boolean checkCollision(GameObject other) {
        return getX() < other.getX() + other.getWidth() &&
                getX() + getWidth() > other.getX() &&
                getY() < other.getY() + other.getHeight() &&
                getY() + getHeight() > other.getY();
    }

    @Override
    public void move() {
        setX(getX() + dx);
        setY(getY() + dy);
        if (getX() <= 0 || getX() + getWidth() >= 800) directionX = -directionX;
        if (getY() <= 0) directionY = -directionY;
    }

    @Override
    public void update() {
        dx = speed * directionX;
        dy = speed * directionY;
        move();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (BALL_IMAGE.isError()) {
            gc.setFill(Color.RED);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        } else {
            gc.drawImage(BALL_IMAGE, getX(), getY(), getWidth(), getHeight());
        }
    }
}
