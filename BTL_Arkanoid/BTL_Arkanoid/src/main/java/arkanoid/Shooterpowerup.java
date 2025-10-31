package arkanoid;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Shooterpowerup extends PowerUp {
    private static final Image POWERUP_IMAGE = new Image(Shooterpowerup.class.getResource("/images/powerup_shooter.png").toString());
    public Shooterpowerup(double x, double y, double width, double height) {
        super(x, y, width, height, "Shooter", 10.0);
    }

    @Override
    public boolean applyEffect(Paddle paddle) {
        return paddle.applyPowerUp(type);
    }

    @Override
    public void removeEffect(Paddle paddle) {
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(POWERUP_IMAGE, getX(), getY(), getWidth(), getHeight());
    }
}

