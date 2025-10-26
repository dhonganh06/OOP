package arkanoid;

import javafx.scene.canvas.GraphicsContext;

public abstract class PowerUp extends GameObject {
    protected String type;
    protected double duration;

    public PowerUp(double x, double y, double width, double height, String type, double duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public abstract boolean applyEffect(Paddle paddle);

    public abstract void removeEffect(Paddle paddle);

    @Override
    public void update() {
        setY(getY() + 2);
    }

    @Override
    public void render(GraphicsContext gc) {}
}
