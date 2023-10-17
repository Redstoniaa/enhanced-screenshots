package snap;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import snap.ui.screen.PostScreenshotScreen;
import snap.utils.IO;
import snap.utils.config.Config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static net.minecraft.text.Text.literal;
import static net.minecraft.util.Formatting.*;
import static snap.utils.Text.translated;

public class Screenshot {
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final Path screenshotDirectory = Path.of(client.runDirectory.getAbsolutePath(), "screenshots");
    
    public final NativeImage image;
    public final Consumer<Text> chatMessageReceiver;
    public final Path unnamedScreenshotFile = createScreenshotFile(Util.getFileNameFormattedDateTime());
    
    public Screenshot(NativeImage image, Consumer<Text> chatMessageReceiver) {
        this.image = image;
        this.chatMessageReceiver = chatMessageReceiver;
    }
    
    public void setup() {
        saveImageUnnamed();
        openUi();
    }
    
    private void openUi() {
        switch (Config.currentSettings.uiType) {
            case SCREEN -> PostScreenshotScreen.open(this);
            case CHAT -> {}
        }
    }
    
    private void saveImageUnnamed() {
        IO.saveNativeImage(image, unnamedScreenshotFile);
    }
    
    public boolean renameImageFile(String fileName) {
        if (fileName.isBlank())
            return true;
        
        Path destination = createScreenshotFile(fileName);
        IO.createParentDirectories(destination);
        String relativePath = screenshotDirectory.relativize(destination).toString();
        if (Files.exists(destination)) {
            sendMessage(translated("snap.screen.rename.failure_file_exists", relativePath).formatted(YELLOW));
            return false;
        }
        
        boolean renameSuccess = IO.rename(unnamedScreenshotFile, destination);
        if (renameSuccess) {
            Text openFileClick = literal(relativePath)
                    .formatted(UNDERLINE)
                    .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, destination.toAbsolutePath().toString())));
            sendMessage(translated("snap.screen.rename.success").formatted(GREEN).append(openFileClick));
        } else {
            sendMessage(translated("snap.screen.rename.failure").formatted(RED));
        }
        
        return renameSuccess;
    }
    
    public boolean copyImageToClipboard() {
        boolean copySuccess = IO.copyImageToClipboard(unnamedScreenshotFile);
    
        if (copySuccess) sendMessage(translated("snap.screen.copy.success").formatted(GREEN));
        else             sendMessage(translated("snap.screen.copy.failure").formatted(RED));
    
        return copySuccess;
    }
    
    public void deleteImage() {
        IO.delete(unnamedScreenshotFile);
        sendMessage(translated("snap.screen.discard.success"));
    }
    
    public void sendMessage(Text message) {
        chatMessageReceiver.accept(message);
    }
    
    public static Path createScreenshotFile(String name) {
        return screenshotDirectory.resolve(name + ".png");
    }
}
