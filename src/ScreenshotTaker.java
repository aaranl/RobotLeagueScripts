import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenshotTaker {
    private int x;
    private int y;
    private int width;
    private int height;
    private Robot robot;
    private JFrame frame;
    private JPanel panel;

    public ScreenshotTaker(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.frame = new JFrame("Screenshot");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, width, height));
                g.drawImage(image, 0, 0, null);
            }
        };
        this.frame.add(this.panel);
        this.frame.setSize(this.width, this.height);
        this.frame.setVisible(true);
    }

    public void start() {
        // Update SS every 100 MS
        while (true) {
            SwingUtilities.invokeLater(() -> panel.repaint());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}