
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player {

    private double x, y;
    private double previousX, previousY;
    private int width, height;
    private double speed = 3.0;
    private boolean left, right, up, down;

    public Player() {
        width = 40;
        height = 40;
        x = GamePanel.WIDTH / 2.0 - width / 2.0;
        y = GamePanel.HEIGHT - height - 20;
        previousX = x;
        previousY = y;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void update() {
        previousX = x;
        previousY = y;

        if (left && x > 0) {
            x -= speed;
        }
        if (right && x < GamePanel.WIDTH - width) {
            x += speed;
        }
        if (up && y > 0) {
            y -= speed;
        }
        if (down && y < GamePanel.HEIGHT - height) {
            y += speed;
        }
    }

    public void draw(Graphics g, double interpolation) {
        double renderX = (x - previousX) * interpolation + previousX;
        double renderY = (y - previousY) * interpolation + previousY;

        g.setColor(Color.WHITE);
        g.fillOval((int)renderX, (int)renderY, width, height);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (key == KeyEvent.VK_UP) {
            up = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            down = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (key == KeyEvent.VK_UP) {
            up = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            down = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
