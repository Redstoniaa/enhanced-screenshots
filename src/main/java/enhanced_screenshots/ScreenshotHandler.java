package enhanced_screenshots;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.text.Text;

import java.io.File;
import java.util.function.Consumer;

public class ScreenshotHandler {
    public static void handleScreenshot(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        messageReceiver.accept(Text.literal("HAHA THWARTED"));
    }
}
