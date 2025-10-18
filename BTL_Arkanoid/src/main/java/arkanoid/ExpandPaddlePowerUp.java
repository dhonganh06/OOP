package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class ExpandPaddlePowerUp extends PowerUp {
    private static final Image POWERUP_IMAGE = new Image(ExpandPaddlePowerUp.class.getResource("/images/powerup_expand.png").toString());
    public ExpandPaddlePowerUp(double x, double y, double width, double height) {
        super(x, y, width, height, "ExpandPaddle", 10.0);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        paddle.applyPowerUp(type);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.setWidth(paddle.getWidth() / 1.5);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(POWERUP_IMAGE, getX(), getY(), getWidth(), getHeight());
    }
}
