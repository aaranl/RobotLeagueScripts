import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class ImageTracker2 {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    // The area on the screen to track the image in
    private Rectangle trackingArea;
    // The image to track
    private BufferedImage icon;
    private BufferedImage entry;
    private BufferedImage screenshot;
    // Robot used to capture the screen to track the image on.
    private Robot robot;
    private Mat iconImg;
    private Mat mapImg;



    //Setter used for all the variables.
    public void setImageTracker2 (Rectangle trackingArea, Mat iconImg, Mat mapImg, BufferedImage icon, BufferedImage bi, BufferedImage screenshot) throws AWTException {
        this.trackingArea = trackingArea;
        this.icon = icon;
        this.robot = new Robot();
        this.entry = entry;
        this.iconImg = imageToMat(icon);
        this.mapImg = imageToMat(screenshot);
        this.screenshot = robot.createScreenCapture(trackingArea);

    }

    public Mat matchingTool (BufferedImage icon, Rectangle trackingArea) throws AWTException {
        Robot robot = new Robot();
        Mat result = new Mat();
        Mat iconImg = imageToMat(icon);
        trackingArea = new Rectangle(trackingArea);
        BufferedImage whatever = robot.createScreenCapture(new Rectangle(trackingArea));
        Mat mapImg = imageToMat(whatever);
        Imgproc.matchTemplate(mapImg, iconImg, result, Imgproc.TM_CCOEFF_NORMED);
        // Convert the result Mat to a supported data type before displaying it
        Mat result8U = new Mat();
        result.convertTo(result8U, CvType.CV_8U);
        HighGui.namedWindow("Result", HighGui.WINDOW_NORMAL);
        HighGui.resizeWindow("Result", 450, 450);
        HighGui.imshow("Result", result8U);
        HighGui.waitKey();


        HighGui.destroyAllWindows();

        return result;
    }


    public Mat imageToMat(BufferedImage inputImg) {
        byte[] pixels;
        if (inputImg.getRaster().getDataBuffer() instanceof DataBufferByte) {
            // BufferedImage uses a byte array to store data
            pixels = ((DataBufferByte) inputImg.getRaster().getDataBuffer()).getData();
        } else if (inputImg.getRaster().getDataBuffer() instanceof DataBufferInt) {
            // BufferedImage uses an int array to store data
            int[] intPixels = ((DataBufferInt) inputImg.getRaster().getDataBuffer()).getData();
            pixels = new byte[intPixels.length * 4];
            for (int i = 0; i < intPixels.length; i++) {
                pixels[i * 4] = (byte) (intPixels[i] >> 24);
                pixels[i * 4 + 1] = (byte) (intPixels[i] >> 16);
                pixels[i * 4 + 2] = (byte) (intPixels[i] >> 8);
                pixels[i * 4 + 3] = (byte) intPixels[i];
            }
        } else {
            // Unsupported DataBuffer type
            throw new UnsupportedOperationException("Unsupported DataBuffer type: " + inputImg.getRaster().getDataBuffer().getClass().getName());
        }

        // Create a Matrix to store the result
        Mat outputImg = new Mat(inputImg.getHeight(), inputImg.getWidth(), CvType.CV_8UC3);

        // Fill the matrix with the BufferedImage data
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
