import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int GRID_SIZE = 20;
    private static final int TILE_SIZE = 20;

    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: right, 1: down, 2: left, 3: up

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        direction = 0;

        spawnFood();

        Timer timer = new Timer(100, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead;

        switch (direction) {
            case 0:
                newHead = new Point((head.x + 1) % GRID_SIZE, head.y);
                break;
            case 1:
                newHead = new Point(head.x, (head.y + 1) % GRID_SIZE);
                break;
            case 2:
                newHead = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                break;
            case 3:
                newHead = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                break;
            default:
                return;
        }

        if (snake.contains(newHead) || newHead.equals(food)) {
            if (newHead.equals(food)) {
                snake.addFirst(food);
                spawnFood();
            } else {
                snake.removeLast();
            }
            snake.addFirst(newHead);
        }
    }

    private void checkGameOver() {
        Point head = snake.getFirst();
        if (snake.size() > 1 && snake.subList(1, snake.size()).contains(head)) {
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.size() - 1));
            System.exit(0);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        Toolkit.getDefaultToolkit().sync();  // Ensure smooth animation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        checkGameOver();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                direction = 0;
                break;
            case KeyEvent.VK_DOWN:
                direction = 1;
                break;
            case KeyEvent.VK_LEFT:
                direction = 2;
                break;
            case KeyEvent.VK_UP:
                direction = 3;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
