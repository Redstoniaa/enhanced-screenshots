package enhanced_screenshots.ui;

import com.mojang.blaze3d.texture.NativeImage;
import enhanced_screenshots.utils.ClipboardUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

import static net.minecraft.text.Text.literal;
import static enhanced_screenshots.EnhancedScreenshotsMod.LOGGER;

public class ScreenshotManagementScreen
        extends Screen {
    public final NativeImage screenshot;
    public final File screenshotDirectory;
    public final File unsavedImageFile;
    public final Consumer<Text> messageReceiver;
    
    public static final int MARGIN = 5;
    
    public TextFieldWidget fileNameField;
    public final ButtonWidget saveButton = ButtonWidget.builder(
                    literal("Save"),
                    button -> saveToFile())
            .size(50, 20)
            .tooltip(Tooltip.create(literal("Save the screenshot as a file")))
            .build();
    public final ButtonWidget copyClipboardButton = ButtonWidget.builder(
                    literal("Copy to Clipboard"),
                    this::copyToClipboard)
            .size(150, 20)
            .tooltip(Tooltip.create(literal("Copy the image to the clipboard")))
            .build();
    public final ButtonWidget discardButton = ButtonWidget.builder(
                    literal("Discard Screenshot"),
                    button -> discardScreenshot())
            .size(150, 20)
            .tooltip(Tooltip.create(literal("Discard the screenshot")))
            .build();
    
    public static void handleScreenshot(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        MinecraftClient.getInstance().setScreen(new ScreenshotManagementScreen(screenshot, screenshotDirectory, messageReceiver));
    }
    
    public ScreenshotManagementScreen(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        super(literal("Screenshot Management"));
        this.screenshot = screenshot;
        this.screenshotDirectory = screenshotDirectory;
        this.messageReceiver = messageReceiver;
        this.unsavedImageFile = new File(screenshotDirectory, "unsaved.png");
        Util.getIoWorkerExecutor()
                .execute(
                        () -> {
                            try (screenshot) {
                                screenshot.writeFile(unsavedImageFile);
                            } catch (Exception e) {
                                LOGGER.warn("Couldn't save screenshot", e);
                            }
                        });
    }
    
    @Override
    protected void init() {
        fileNameField = new TextFieldWidget(
                textRenderer,
                0, 0,
                100, 20,
                Text.literal("hi"));
        add(fileNameField, MARGIN, MARGIN);
        add(saveButton, MARGIN + 100, MARGIN);
        add(copyClipboardButton, MARGIN, MARGIN + 20);
        add(discardButton, MARGIN, MARGIN + 40);
    }
    
    private void add(ClickableWidget widget, int x, int y) {
        widget.setPosition(x, y);
        addDrawableChild(widget);
    }
    
    @Override
    public void closeScreen() {
        discardScreenshot();
    }
    
    private void saveToFile() {
        String finalFileName = fileNameField.getText() + ".png";
        File finalFile = new File(screenshotDirectory, finalFileName);
        unsavedImageFile.renameTo(finalFile);
        
        messageReceiver.accept(literal("Saved screenshot as " + finalFileName).formatted(Formatting.GREEN));
        super.closeScreen();
    }
    
    private void copyToClipboard(ButtonWidget button) {
        try {
            if (!GraphicsEnvironment.isHeadless()) {
                BufferedImage image = ImageIO.read(unsavedImageFile);
                ClipboardUtil.copyToClipboard(image);
                messageReceiver.accept(Text.literal("Screenshot copied to clipboard!").formatted(Formatting.GREEN));
            } else {
                messageReceiver.accept(Text.literal("Head lost").formatted(Formatting.RED));
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageReceiver.accept(Text.literal("Failed to copy screenshot to clipboard.").formatted(Formatting.RED));
        }
        
        button.setMessage(Text.literal("Copied!"));
        button.active = false;
    }
    
    private void discardScreenshot() {
        unsavedImageFile.delete();
        
        messageReceiver.accept(literal("Screenshot discarded.").formatted(Formatting.YELLOW));
        super.closeScreen();
    }
}
