package enhanced_screenshots.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.texture.NativeImage;
import enhanced_screenshots.gui.screen.ScreenshotManagementScreen;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotRecorderMixin {
    @Inject(method = "saveScreenshotInner",
            at = @At(value = "INVOKE", target = "Ljava/io/File;mkdir()Z", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void grab(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci, NativeImage nativeImage, File file) {
        ci.cancel();
        ScreenshotManagementScreen.open(nativeImage, file, messageReceiver);
    }
}
