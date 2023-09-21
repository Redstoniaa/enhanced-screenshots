package enhanced_screenshots.utils.file;

import com.mojang.blaze3d.texture.NativeImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.File;

import static enhanced_screenshots.EnhancedScreenshotsMod.LOGGER;

public class Files {
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    public static void saveNativeImage(NativeImage image, File destination) {
        try (image) {
            image.writeFile(destination);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("An error occurred while trying to save the image.");
        }
    }
    
    public static boolean renameFile(File file, File destination) {
        boolean renameWasSuccessful = false;
        try {
            renameWasSuccessful = file.renameTo(destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!renameWasSuccessful)
            LOGGER.error("An error occurred while renaming the file.");
        return renameWasSuccessful;
    }
    
    public static boolean copyImageToClipboard(File source) {
        if (GraphicsEnvironment.isHeadless()) {
            LOGGER.error("The GraphicsEnvironment has been set to headless. Cannot copy image to clipboard.");
            return false;
        }
        
        BufferedImage image;
        try {
            image = ImageIO.read(source);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("An error occurred trying to read the image to copy to clipboard.");
            return false;
        }
    
        copyImageToClipboard(image);
        return true;
    }
    
    private static void copyImageToClipboard(BufferedImage img) {
        clipboard.setContents(new TransferableImage(img), null);
    }
}
