package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bomb extends PowerUp {

    private static final Image BOMB_IMAGE = new Image(Bomb.class.getResource("/images/bomb.png").toString());
    public Bomb(double x, double y, double width, double height) {
        super(x, y, width, height, "Bomb", 0);
    }

    @Override
    public boolean applyEffect(Paddle paddle) {
        return false;
    }

    @Override
    public void removeEffect(Paddle paddle) {
    }

    @Override
    public void render(GraphicsContext gc) {
        if (BOMB_IMAGE.isError()) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        } else {
            gc.drawImage(BOMB_IMAGE, getX(), getY(), getWidth(), getHeight());
        }
    }
}