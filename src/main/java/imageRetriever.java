
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class imageRetriever {

    //This function resizes the image so that any image we get from API of any aspect ratio can be resized appropriately so that it can be displayed in GUI
    public static BufferedImage resizeImageByHeight(BufferedImage originalImage, int targetHeight) {
        double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
        int targetWidth = (int) (targetHeight * aspectRatio);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }

    //This function retrieves an icon from a url
    public static Icon iconRetriever(String urlLocation) {
        BufferedImage image1 = null;
        try {
            URL url = new URL(urlLocation);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            conn.connect();
            InputStream urlStream = conn.getInputStream();
            image1=ImageIO.read(urlStream);
            int targetheight=100;
            BufferedImage image=resizeImageByHeight(image1,targetheight);
            ImageIcon icon=new ImageIcon(image);

            return icon;


        } catch (IOException e) {
            System.out.println("Something went wrong, sorry:" + e.toString());
        }
        return null;
    }

    //retrieves a bufferedImage from a URL
    public static BufferedImage bufferedImageRetriever(String urlLocation) {
        BufferedImage image1 = null;
        try {
            URL url = new URL(urlLocation);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            conn.connect();
            InputStream urlStream = conn.getInputStream();
            image1=ImageIO.read(urlStream);
            int targetheight=100;

            return resizeImageByHeight(image1,targetheight);


        } catch (IOException e) {
            System.out.println("Something went wrong, sorry:" + e.toString());
        }
        return null;
    }

    //Writes an image to a .png file
    public static void imageWriter(File outputFile,BufferedImage bufferedImage,String name){
        // Write the image to a file
        try {
            outputFile = new File("src/main/resources/"+name+"_image.png");
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Image has been written successfully.");
        } catch (IOException e) {
            System.err.println("Error writing the song image: " + e.getMessage());
        }
    }
}


