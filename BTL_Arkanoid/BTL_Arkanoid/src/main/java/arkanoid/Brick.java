package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Lớp Brick đại diện cho gạch cần phá
public class Brick extends GameObject {
    private int hitPoints;
    private String type;
    private static final Image NORMAL_BRICK_IMAGE = new Image(Brick.class.getResource("/images/brick_normal.png").toString());
    private static final Image STRONG_BRICK_IMAGE = new Image(Brick.class.getResource("/images/brick_strong.png").toString());

    public Brick(double x, double y, double width, double height, String type, int hitPoints) {
        super(x, y, width, height);
        this.type = type;
        this.hitPoints = hitPoints;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {}

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            Image brickImage = type.equals("Normal") ? NORMAL_BRICK_IMAGE : STRONG_BRICK_IMAGE;
            gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());
        }
    }
}
