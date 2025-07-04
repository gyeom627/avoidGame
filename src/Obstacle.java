
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Obstacle {

    private double x, y;
    private double previousX, previousY;
    private int width, height;
    private double speed;
    private double dx, dy; // Movement direction
    private Random rand;

    public Obstacle(int spawnLocation, double speed) {
        rand = new Random();
        width = 10;
        height = 10;
        this.speed = speed;

        double targetX = rand.nextInt(GamePanel.WIDTH);
        double targetY = rand.nextInt(GamePanel.HEIGHT);

        // Set initial position based on spawn location
        switch (spawnLocation) {
            case 0: // Top
                x = rand.nextInt(GamePanel.WIDTH - width);
                y = -height;
                break;
            case 1: // Left
                x = -width;
                y = rand.nextInt(GamePanel.HEIGHT - height);
                break;
            case 2: // Right
                x = GamePanel.WIDTH;
                y = rand.nextInt(GamePanel.HEIGHT - height);
                break;
            case 3: // Bottom
                x = rand.nextInt(GamePanel.WIDTH - width);
                y = GamePanel.HEIGHT;
                break;
            case 4: // Top-Left Quadrant
                x = rand.nextInt(GamePanel.WIDTH / 2);
                y = -height;
                targetX = rand.nextInt(GamePanel.WIDTH / 2) + GamePanel.WIDTH / 2;
                targetY = rand.nextInt(GamePanel.HEIGHT / 2) + GamePanel.HEIGHT / 2;
                break;
            case 5: // Top-Right Quadrant
                x = rand.nextInt(GamePanel.WIDTH / 2) + GamePanel.WIDTH / 2;
                y = -height;
                targetX = rand.nextInt(GamePanel.WIDTH / 2);
                targetY = rand.nextInt(GamePanel.HEIGHT / 2) + GamePanel.HEIGHT / 2;
                break;
            case 6: // Bottom-Left Quadrant
                x = rand.nextInt(GamePanel.WIDTH / 2);
                y = GamePanel.HEIGHT;
                targetX = rand.nextInt(GamePanel.WIDTH / 2) + GamePanel.WIDTH / 2;
                targetY = rand.nextInt(GamePanel.HEIGHT / 2);
                break;
            case 7: // Bottom-Right Quadrant
                x = rand.nextInt(GamePanel.WIDTH / 2) + GamePanel.WIDTH / 2;
                y = GamePanel.HEIGHT;
                targetX = rand.nextInt(GamePanel.WIDTH / 2);
                targetY = rand.nextInt(GamePanel.HEIGHT / 2);
                break;
        }
        previousX = x;
        previousY = y;

        double dirX = targetX - x;
        double dirY = targetY - y;

        double magnitude = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitude > 0) {
            dx = (dirX / magnitude) * speed;
            dy = (dirY / magnitude) * speed;
        }
    }

    public void update() {
        previousX = x;
        previousY = y;
        x += dx;
        y += dy;
    }

    public void draw(Graphics g, double interpolation) {
        double renderX = (x - previousX) * interpolation + previousX;
        double renderY = (y - previousY) * interpolation + previousY;
        g.setColor(Color.RED);
        g.fillRect((int)renderX, (int)renderY, width, height);
    }

    public boolean isOffScreen() {
        return y > GamePanel.HEIGHT + 50 || y < -height - 50 || x > GamePanel.WIDTH + 50 || x < -width - 50;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public double getSpeed() {
        return speed;
    }
}
