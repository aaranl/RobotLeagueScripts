//import org.opencv.core.Point;
//import org.opencv.core.*;
//import org.opencv.highgui.HighGui;
//import org.opencv.imgproc.Imgproc;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferByte;
//import java.awt.image.DataBufferInt;
//
//public class ImageTracker {
//
//    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
//
//    // The area on the screen to track the image in
//    private Rectangle trackingArea;
//
//    // The image to track
//    private BufferedImage image;
//
//    // The "enemy icon" image to track
//    private BufferedImage enemyIcon;
//
//    // The radius around the image to track the enemy icon in
//    private int radius;
//
//    // The Robot instance used to capture screenshots
//    private Robot robot;
//
//    public ImageTracker(BufferedImage image, BufferedImage enemyIcon, int radius, Rectangle trackingArea) throws AWTException {
//        this.image = image;
//        this.enemyIcon = enemyIcon;
//        this.radius = radius;
//        this.trackingArea = trackingArea;
//        this.robot = new Robot();
//    }
//
//    // Returns the location of the tracked image on the screen, or null if
//    // the image is not found in the tracking area.
//    public Point getLocation() {
//        BufferedImage screenshot = robot.createScreenCapture(trackingArea);
//
//        // Find the location of the original image in the screenshot
//        Point imageLocation = findImage(screenshot, image);
//        Mat mat = Mat.zeros(trackingArea.height, trackingArea.width, CvType.CV_8UC3);
//
//// Draw a circle at the center of the tracked image
//        Imgproc.circle(mat, imageLocation, 10, new Scalar(0, 0, 255), -1);
//
//// Display the image in a window
//        HighGui.imshow("Tracked Image", mat);
//
//// Wait for 100 milliseconds before updating the window
//        HighGui.waitKey(100);
//        return imageLocation;
//
//    }
//    private Point findImage(BufferedImage screenshot, BufferedImage image) {
//        Mat screenshotMat = bufferedImageToMat(screenshot);
//        Mat imageMat = bufferedImageToMat(image);
//
//        // Convert the images to grayscale
//        Imgproc.cvtColor(screenshotMat, screenshotMat, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
//
//        // Create the result matrix
//        int resultCols = screenshotMat.cols() - imageMat.cols() + 1;
//        int resultRows = screenshotMat.rows() - imageMat.rows() + 1;
//        Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);
//
//        // Do the template matching
//        Imgproc.matchTemplate(screenshotMat, imageMat, result, Imgproc.TM_CCOEFF_NORMED);
//
//        // Normalize the result matrix
//        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
//
//        // Find the best match
//        Core.MinMaxLocResult minMaxResult = Core.minMaxLoc(result);
//        Point matchLocation = minMaxResult.maxLoc;
//
//        // Convert the match location to a point in the original image coordinates
//        Point imageLocation = new Point(matchLocation.x + imageMat.cols() / 2, matchLocation.y + imageMat.rows() / 2);
//
//        return imageLocation;
//    }
//
//
//    //******************
//
//    private static Mat bufferedImageToMat(BufferedImage bi) {
//        Mat mat = new Mat();
//        int type = bi.getType();
//        if (type == BufferedImage.TYPE_3BYTE_BGR || type == BufferedImage.TYPE_4BYTE_ABGR) {
//            // Byte-based image
//            byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
//            int channels = 3;
//            int width = bi.getWidth();
//            int height = bi.getHeight();
//            int rowBytes = width * channels;
//            if (data.length == rowBytes * height) {
//                // Data array is the correct size
//                mat = new Mat(height, width, CvType.CV_8UC3);
//                mat.put(0, 0, data);
//            } else {
//                // Data array is the incorrect size
//                mat = new Mat(height, width, CvType.CV_8UC3);
//                byte[] paddedData = new byte[rowBytes * height];
//                int copyLength = Math.min(data.length, paddedData.length);
//                System.arraycopy(data, 0, paddedData, 0, copyLength);
//                mat.put(0, 0, paddedData);
//            }
//        } else if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB) {
//            // Integer-based image
//            int[] data = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
//            int channels = 3;
//            int width = bi.getWidth();
//            int height = bi.getHeight();
//            int rowInts = width * channels;
//            if (data.length == rowInts * height) {
//                // Data array is the correct size
//                mat = new Mat(height, width, CvType.CV_8UC3);
//                byte[] byteData = new byte[rowInts * height];
//                for (int i = 0; i < data.length; i++) {
//                    byteData[i] = (byte) (data[i] & 0xFF);
//                }
//                mat.put(0, 0, byteData);
//            } else {
//                // Data array is the incorrect size
//                mat = new Mat(height, width, CvType.CV_8UC3);
//                int[] paddedData = new int[rowInts * height];
//                int copyLength = Math.min(data.length, paddedData.length);
//                System.arraycopy(data, 0, paddedData, 0, copyLength);
//                byte[] byteData = new byte[rowInts * height];
//                for (int i = 0; i < paddedData.length; i++) {
//                    byteData[i] = (byte) (paddedData[i] & 0xFF);
//                }
//                mat.put(0, 0, byteData);
//            }
//
//// Convert the integer-based Mat to a byte-based Mat
//            if (mat.type() == CvType.CV_32SC3) {
//                Mat byteMat = new Mat();
//                Core.convertScaleAbs(mat, byteMat, 1.0 / 256.0, 0);
//                return byteMat;
//            } else {
//                return mat;
//            }
//        }
//        return mat;
//    }
//    // ***************
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    //****************
////    private Point findImageWithinRadius(BufferedImage screenshot, BufferedImage image, Point center, int radius) {
////        // Crop the screenshot to a region around the center point with the specified radius
////        BufferedImage croppedScreenshot = screenshot.getSubimage((int)center.x - radius, (int)center.y - radius, radius * 2, radius * 2);
////
////        // Find the location of the image in the cropped screenshot using template matching
////        Point imageLocation = findImage(croppedScreenshot, image);
////
////        if (imageLocation != null) {
////            // If the image is found, adjust the location to account for the cropping
////            imageLocation.x += center.x - radius;
////            imageLocation.y += center.y - radius;
////        }
////
////        return imageLocation;
////    }
//
////     private Point findImage(BufferedImage screenshot, BufferedImage image) {
////         // Loop over every pixel in the screenshot
////         for (int x = 0; x < screenshot.getWidth() - image.getWidth(); x++) {
////             for (int y = 0; y < screenshot.getHeight() - image.getHeight(); y++) {
////                 // Check if the current pixel is the start of the image
////                 if (isStartOfImage(screenshot, image, x, y)) {
////                     // If it is, return the location of the start pixel
////                     return new Point(x, y);
////                 }
////             }
////         }
////
////         // If the image is not found, return null
////         return null;
////     }
////
////     private Point findImageWithinRadius(BufferedImage screenshot, BufferedImage image, Point center, int radius) {
////         // Calculate the minimum and maximum x and y values to search
////         int minX = Math.max(center.x - radius, 0);
////         int maxX = Math.min(center.x + radius, screenshot.getWidth() - image.getWidth());
////         int minY = Math.max(center.y - radius, 0);
////         int maxY = Math.min(center.y + radius, screenshot.getHeight() - image.getHeight());
////
////         // Loop over the search area
////         for (int x = minX; x <= maxX; x++) {
////             for (int y = minY; y <= maxY; y++) {
////                 // Check if the current pixel is the start of the image
////                 if (isStartOfImage(screenshot, image, x, y)) {
////                     // If it is, return the location of the start pixel
////                     return new Point(x, y);
////                 }
////             }
////         }
////
////         // If the image is not found, return null
////         return null;
////     }
//
////    // Returns true if the pixel at (x, y) in the screenshot is the start of the image
////    private boolean isStartOfImage(BufferedImage screenshot, BufferedImage image, int x, int y) {
////        // Loop over every pixel in the image
////        for (int i = 0; i < image.getWidth(); i++) {
////            for (int j = 0; j < image.getHeight(); j++) {
////                // If the pixel in the screenshot does not match the pixel in the image, return false
////                if (screenshot.getRGB(x + i, y + j) != image.getRGB(i, j)) {
////                    return false;
////                }
////            }
////        }
////
////        // If all pixels match, return true
////        return true;
////    }
//    //****************
//
//    // Plays the sound
//    private void playSound() {
//        // Implement sound playing code here
//        // ...
//    }
//}