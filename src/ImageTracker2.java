import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


public class ImageTracker2 {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    // The area on the screen to track the image in
    private Rectangle trackingArea;
    // The image to track
    private BufferedImage icon;
    private BufferedImage entry;
    // Robot used to capture the screen to track the image on.
    private Robot robot;





    //Setter used for all the variables.
    public ImageTracker2 (Rectangle trackingArea, BufferedImage icon, BufferedImage entry) throws AWTException {
        this.trackingArea = trackingArea;
        this.icon = icon;
        this.robot = new Robot();
        this.entry = entry;

    }

//    public Point iconTracker (Rectangle trackingArea, BufferedImage icon) {
//
//        //Captures screenshot that match the selected trackingArea to use.
//        BufferedImage screenshot = robot.createScreenCapture(trackingArea);
//
//        // Find the location of the original image in the screenshot
//        Point imageLocation = findImage(screenshot, icon);
//        Mat mat = Mat.zeros(trackingArea.height, trackingArea.width, CvType.CV_8UC3);
//
//        // Draw a circle at the center of the tracked image
//        Imgproc.circle(mat, imageLocation, 10, new Scalar(0, 0, 255), -1);
//        // Display the image in a window
//        HighGui.imshow("Tracked Image", mat);
//
//        // Wait for 100 milliseconds before updating the window
//        HighGui.waitKey(100);
//        return imageLocation;
//    }

    public Mat imageToMatMatrix(BufferedImage entry) {
        BufferedImage inputImg = entry;
        int width = inputImg.getWidth();
        int height = inputImg.getHeight();
        int[] pixels = new int[width * height];
        inputImg.getRGB(0, 0, width, height, pixels, 0, width);

        Mat outputImg = new Mat(height, width, CvType.CV_8UC3, new Scalar(0, 0, 0));
        outputImg.put(0, 0, pixels);
        return outputImg;
    }

    public BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] buffer = new byte[bufferSize];
        matrix.get(0, 0, buffer); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);

        return image;
    }


}
