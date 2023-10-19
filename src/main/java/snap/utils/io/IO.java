package snap.utils.io;

import com.mojang.blaze3d.texture.NativeImage;
import snap.utils.TransferableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static snap.SnapMod.LOGGER;

public class IO {
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
    
    public static boolean createParentDirectories(Path path) {
        return createDirectories(path.getParent());
    }
    
    public static boolean createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception exception) {
            return false;
        }
        
        return true;
    }
    
    public static void saveNativeImage(NativeImage image, Path destination) {
        try (image) {
            image.writeFile(destination);
        } catch (Exception e) {
            LOGGER.error("An error occurred while trying to save the image.", e);
        }
    }
    
    public static boolean rename(Path target, Path destination) {
        try {
            Files.move(target, destination);
            return true;
        } catch (Exception e) {
            LOGGER.error("An error occurred while renaming the file.", e);
            return false;
        }
    }
    
    public static boolean copyImageToClipboard(Path source) {
        if (isHeadless) return false;
        
        BufferedImage image = readImageFrom(source);
        if (image == null) return false;
        
        copyImageToClipboard(image);
        return true;
    }
    
    public static boolean delete(Path target) {
        try {
            Files.delete(target);
            return true;
        } catch (IOException e) {
            LOGGER.error("An IO exception occurred while trying to delete the file at " + target, e);
            return false;
        }
    }
    
    private static BufferedImage readImageFrom(Path path) {
        try {
            return ImageIO.read(Files.newInputStream(path));
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to read the image at " + path, e);
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
