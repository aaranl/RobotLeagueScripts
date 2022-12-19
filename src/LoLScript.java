import org.opencv.core.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoLScript {
    public static void main(String[] args) throws IOException {
        try {
            Robot robot = new Robot();

            BufferedImage leagueEzreal = ImageIO.read(new File("src/LeagueEzreal.png"));
            BufferedImage leagueEpic = ImageIO.read(new File("src/LeagueEzreal.png"));
            Rectangle trackingArea = new Rectangle(1500, 660, 430, 400);

            ImageTracker imageTracker = new ImageTracker(leagueEzreal, leagueEpic, 0, trackingArea);

            while (true) {
                // Get the location of the image
                Point imageLocation = imageTracker.getLocation();

                // Print the location of the image
                System.out.println(imageLocation);

                // Sleep for 100 milliseconds
                Thread.sleep(100);
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

