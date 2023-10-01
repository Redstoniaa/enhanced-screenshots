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
    public static final boolean isHeadless;
    private static final Clipboard clipboard;
    
    static {
        isHeadless = GraphicsEnvironment.isHeadless();
        if (isHeadless) {
            LOGGER.error("The GraphicsEnvironment has been set to headless. Clipboard functionality is unavailable.");
            clipboard = null;
        } else {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        }
    }
    
    public static void saveNativeImage(NativeImage image, File destination) {
        try (image) {
            image.writeFile(destination);
        } catch (Exception e) {
            LOGGER.error("An error occurred while trying to save the image.", e);
        }
    }
    
    public static boolean rename(File target, File destination) {
        try {
            boolean renameSuccess = target.renameTo(destination);
            if (!renameSuccess)
                LOGGER.error("File rename operation failed. Attempted to rename " + target + " to " + destination);
            return renameSuccess;
        } catch (Exception e) {
            LOGGER.error("An error occurred while renaming the file.", e);
            return false;
        }
    }
    
    public static boolean copyImageToClipboard(File source) {
        if (isHeadless) return false;
        
        BufferedImage image = readImageFrom(source);
        if (image == null) return false;
        
        copyImageToClipboard(image);
        return true;
    }
    
    private static BufferedImage readImageFrom(File file) {
        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to read the image at " + file, e);
            return null;
        }
    }
    
    private static void copyImageToClipboard(BufferedImage image) {
        copyToClipboard(new TransferableImage(image));
    }
    
    private static void copyToClipboard(Transferable object) {
        clipboard.setContents(object, null);
    }
}
