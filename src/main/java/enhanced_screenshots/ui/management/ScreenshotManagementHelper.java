package enhanced_screenshots.ui.management;

import com.mojang.blaze3d.texture.NativeImage;
import enhanced_screenshots.utils.Clipboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static enhanced_screenshots.EnhancedScreenshotsMod.LOGGER;

public class ScreenshotManagementHelper {
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
    
    public static void saveScreenshot(NativeImage screenshot, File file) {
        Util.getIoWorkerExecutor()
                .execute(() -> {
                    try (screenshot) {
                        screenshot.writeFile(file);
                    } catch (Exception e) {
                        LOGGER.warn("Couldn't save screenshot", e);
                    }
                });
    }
    
    public static boolean renameFile(File file, File destination) {
        try {
            boolean renameSuccess = file.renameTo(destination);
            if (!renameSuccess) {
                LOGGER.warn("Failed to rename file.");
                return false;
            }
        } catch (Exception e) {
            LOGGER.warn("An error occurred while renaming the file.", e);
            return false;
        }
        
        return true;
    }
    
    public static boolean copyImageToClipboard(File file) {
        try {
            if (!GraphicsEnvironment.isHeadless()) {
                BufferedImage image = ImageIO.read(file);
                Clipboard.copyToClipboard(image);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
