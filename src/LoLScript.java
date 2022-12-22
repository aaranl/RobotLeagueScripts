import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class LoLScript {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) throws Exception {
        try {
            String inFile = "champions/Ezreal_OriginalCircle.png";
            String template = "screenshot.png";
            String outFile = "cool.png";
            Rectangle trackingArea = new Rectangle(1500, 650, 450, 450);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Robot robot = new Robot();

            while (true) {
                BufferedImage screenCapture = robot.createScreenCapture(trackingArea);
                ImageIO.write(screenCapture, "png", new File("screenshot.png"));
                new MatchTemplate().run(template, inFile, outFile, Imgproc.TM_CCOEFF);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
