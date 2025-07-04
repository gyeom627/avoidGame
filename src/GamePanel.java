
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    private Thread gameThread;
    private boolean running = false;
    private boolean gameOver = false;

    private Player player;
    private CopyOnWriteArrayList<Obstacle> obstacles;

    private Random rand;
    private long lastSpawnTime;
    private long spawnDelay;
    private long gameStartTime;
    private int score;
    private double obstacleSpeed;

    // Game Loop variables
    private final int TICKS_PER_SECOND = 60;
    private final int SKIP_TICKS = 1000000000 / TICKS_PER_SECOND;
    private final int MAX_FRAMESKIP = 5;
    private double interpolation = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        rand = new Random();
        player = new Player();
        obstacles = new CopyOnWriteArrayList<>();
        resetGame();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    @Override
    public void run() {
        long next_game_tick = System.nanoTime();
        int loops;

        while (running) {
            loops = 0;
            while (System.nanoTime() > next_game_tick && loops < MAX_FRAMESKIP) {
                if (!gameOver) {
                    update();
                }
                next_game_tick += SKIP_TICKS;
                loops++;
            }

            interpolation = (double) (System.nanoTime() + SKIP_TICKS - next_game_tick)
                            / (double) SKIP_TICKS;
            
            repaint();
        }
    }

    public void update() {
        player.update();

        long currentTime = System.currentTimeMillis();
        long gameDuration = currentTime - gameStartTime;

        // Difficulty progression
        int spawnLocationRange = 1; // Top only
        if (gameDuration > 10000) { 
            spawnLocationRange = 4; // Top, Left, Right, Bottom
        }
        if (gameDuration > 20000) { 
            spawnLocationRange = 8; // All directions
            obstacleSpeed = 3.0 + (gameDuration - 20000) / 10000.0;
            player.setSpeed(obstacleSpeed);
        }
        
        if (gameDuration > 40000) {
            spawnDelay = rand.nextInt(200) + 50;
        } else {
            spawnDelay = rand.nextInt(300) + 100;
        }

        if (currentTime - lastSpawnTime > spawnDelay) {
            obstacles.add(new Obstacle(rand.nextInt(spawnLocationRange), obstacleSpeed));
            lastSpawnTime = currentTime;
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle o = obstacles.get(i);
            o.update();
            if (o.isOffScreen()) {
                obstacles.remove(i);
                score += (int)o.getSpeed();
                i--;
            } else if (o.getBounds().intersects(player.getBounds())) {
                gameOver = true;
            }
        }
    }

    private void resetGame() {
        player = new Player();
        obstacles.clear();
        score = 0;
        gameOver = false;
        gameStartTime = System.currentTimeMillis();
        lastSpawnTime = System.currentTimeMillis();
        spawnDelay = 500;
        obstacleSpeed = 3.0;
        player.setSpeed(3.0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double renderInterpolation = gameOver ? 1.0 : interpolation;
        player.draw(g, renderInterpolation);
        for (Obstacle o : obstacles) {
            o.draw(g, renderInterpolation);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // Draw developer credit
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String devText = "Developer: KO";
        int textWidth = g.getFontMetrics().stringWidth(devText);
        g.drawString(devText, WIDTH - textWidth - 10, 20);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.fillRect(WIDTH / 2 - 200, HEIGHT / 2 - 100, 400, 200);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", WIDTH / 2 - 150, HEIGHT / 2 - 30);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, WIDTH / 2 - 70, HEIGHT / 2 + 20);
            g.drawString("Press 'R' to Restart", WIDTH / 2 - 100, HEIGHT / 2 + 50);
            g.drawString("Press 'Q' to Quit", WIDTH / 2 - 90, HEIGHT / 2 + 80);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                resetGame();
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                System.exit(0);
            }
        } else {
            player.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
