package enhanced_screenshots.utils.file;

import com.mojang.blaze3d.texture.NativeImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
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
    
    public static boolean rename(File target, File destination) {
        boolean wasRenameSuccessful = false;
        try {
            wasRenameSuccessful = target.renameTo(destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!wasRenameSuccessful)
            LOGGER.error("An error occurred while renaming the file.");
        return wasRenameSuccessful;
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
    
    private static void copyImageToClipboard(BufferedImage image) {
        copyToClipboard(new TransferableImage(image));
    }
    
    private static void copyToClipboard(Transferable object) {
        clipboard.setContents(object, null);
    }
}
