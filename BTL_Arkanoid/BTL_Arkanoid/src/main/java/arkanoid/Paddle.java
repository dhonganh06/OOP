package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Paddle extends MovableObject {
    private double speed;
    private boolean isShooterActive = false;
    private double GunWidth = 10;
    private double GunHeight = 20;
    private int bulletcount = 0;
    private int expandCount = 0;
    private static final int MAX_EXPAND_COUNT = 4;
    private String currentPowerUp;
    private static final Image PADDLE_IMAGE = new Image(Paddle.class.getResource("/images/paddle.png").toString());
    private static final Image GUN_IMAGINE = new Image(Paddle.class.getResource("/images/gun.png").toString());
    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 5.0;
        this.currentPowerUp = null;
    }

    public void moveLeft() { dx = -speed; }

    public void moveRight() { dx = speed; }

    public void stop() { dx = 0; }
    public void removeShooterEffect() {
        isShooterActive = false;
        bulletcount = 0;
    }

    public boolean applyPowerUp(String powerUpType) {
        this.currentPowerUp = powerUpType;
        if (powerUpType.equals("ExpandPaddle")) {
            if(expandCount <= MAX_EXPAND_COUNT) {
            setWidth(getWidth() + 40);
            expandCount++;
            return true;
            }
        else {
            return false;
        }
        }else if(powerUpType.equals("Shooter")) {
            isShooterActive = true;
            bulletcount +=3;
            return true;
        }
        return false;
    }
    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        if (isShooterActive&& bulletcount > 0) {
            double bulletWidth = 5;
            double bulletHeight = 10;
            bullets.add(new Bullet(getX() + (GunWidth / 2) - (bulletWidth / 2), getY(), bulletWidth, bulletHeight));
            bullets.add(new Bullet(getX() + getWidth() - (GunWidth / 2) - (bulletWidth / 2), getY(), bulletWidth, bulletHeight));
            bulletcount--;
            if(bulletcount <=0){
                isShooterActive = false;}
        }
        return bullets;
    }
    public int getBulletCount() {
        return this.bulletcount;
    }
    public boolean isShooterActive() {
        return this.isShooterActive;
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
        if (isShooterActive) {
            gc.drawImage(GUN_IMAGINE, getX(), getY() - (GunHeight/2), GunWidth, GunHeight);
            gc.drawImage(GUN_IMAGINE, getX() + getWidth() - GunWidth, getY() - (GunHeight/2), GunWidth, GunHeight);
        }
    }
}
