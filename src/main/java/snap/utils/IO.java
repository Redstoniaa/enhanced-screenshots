package snap.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.texture.NativeImage;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static snap.SnapMod.LOGGER;

public class IO {
    private static final ClassLoader CLASS_LOADER = Translations.class.getClassLoader();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
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
    
    public static <T> T readJsonFromResource(String path, TypeToken<T> typeToken) throws IOException {
        InputStream stream = getResourceAsStream(path)
                .orElseThrow(() -> new IOException("Resource could not be found or accessed."));
        String content = readInputStream(stream);
        return readJsonFromString(content, typeToken);
    }
    
    private static Optional<InputStream> getResourceAsStream(String path) {
        InputStream inputStream = CLASS_LOADER.getResourceAsStream(path);
        return Optional.ofNullable(inputStream);
    }
    
    public static <T> T readJsonFromPath(Path path, TypeToken<T> typeToken) throws IOException {
        String content = Files.readString(path);
        return IO.readJsonFromString(content, typeToken);
    }
    
    public static void writeJsonToFile(Object object, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        GSON.toJson(object, writer);
        writer.flush();
        writer.close();
    }
    
    public static <T> T readJsonFromString(String input, TypeToken<T> typeToken) {
        return GSON.fromJson(input, typeToken);
    }
    
    private static String readInputStream(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
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
