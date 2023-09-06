package enhanced_screenshots.ui;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.io.File;
import java.util.function.Consumer;

import static net.minecraft.text.Text.literal;

public class ScreenshotManagementScreen
        extends Screen {
    public final NativeImage screenshot;
    public final File screenshotDirectory;
    public final Consumer<Text> messageReceiver;
    
    public static final int MARGIN = 5;
    
    public TextFieldWidget fileNameField;
    public final ButtonWidget saveButton = ButtonWidget.builder(
                    literal("Save"),
                    button -> init())
            .size(50, 20)
            .tooltip(Tooltip.create(literal("Save the screenshot as a file")))
            .build();
    public final ButtonWidget copyClipboardButton = ButtonWidget.builder(
                    literal("Copy to Clipboard"),
                    button -> init())
            .size(150, 20)
            .tooltip(Tooltip.create(literal("Copy the image to the clipboard")))
            .build();
    public final ButtonWidget discardButton = ButtonWidget.builder(
                    literal("Discard Screenshot"),
                    button -> closeScreen())
            .size(150, 20)
            .tooltip(Tooltip.create(literal("Discard the screenshot")))
            .build();
    
    public static void handleScreenshot(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        MinecraftClient.getInstance()
                .setScreen(new ScreenshotManagementScreen(screenshot, screenshotDirectory, messageReceiver));
    }
    
    public ScreenshotManagementScreen(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        super(literal("Screenshot Management"));
        this.screenshot = screenshot;
        this.screenshotDirectory = screenshotDirectory;
        this.messageReceiver = messageReceiver;
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
        messageReceiver.accept(literal("Screenshot discarded."));
        super.closeScreen();
    }
}
