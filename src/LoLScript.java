import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class LoLScript {
    public static void main(String[] args) throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle trackingArea = new Rectangle(1500, 660, 450, 450);

        ImageTracker2 answer = new ImageTracker2();




        BufferedImage entry = null;
        BufferedImage icon = ImageIO.read(new File("src/LeagueEzreal.png"));
        BufferedImage screenshot = robot.createScreenCapture(trackingArea);
        Mat mapImg = answer.imageToMat(screenshot);
        Mat iconImg = answer.imageToMat(icon);

        answer.setImageTracker2(trackingArea, iconImg, mapImg, icon, entry, screenshot);

        answer.matchingTool(icon, trackingArea);


        answer.setImageTracker2(trackingArea, iconImg, mapImg, icon, entry, screenshot);



        //ScreenshotTaker taker = new ScreenshotTaker(1500, 660, 450, 450);
       // taker.start();



    }
}