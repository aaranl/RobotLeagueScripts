import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoLScript {
    public static void main(String[] args) {
        // Set the dimensions of the region of the screen that you want to capture
        int x = 1500;
        int y = 660;
        int width = 450;
        int height = 450;
        Rectangle screenRect = new Rectangle(x, y, width, height);

        // Create a Robot object to take the screenshot
        Robot robot;
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Set up a JFrame and JPanel to display the screenshot
        JFrame frame = new JFrame("Screenshot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                BufferedImage image = robot.createScreenCapture(screenRect);
                g.drawImage(image, 0, 0, null);
            }
        };
        frame.add(panel);
        frame.setSize(450, 450);
        frame.setVisible(true);

        // Update the screenshot every 100 milliseconds
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