package enhanced_screenshots.ui.management;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.util.function.Consumer;

import static enhanced_screenshots.ui.management.ScreenshotManagementHelper.*;
import static net.minecraft.text.Text.literal;
import static net.minecraft.util.Formatting.*;

public class ScreenshotManagementScreen
        extends Screen {
    private final NativeImage screenshot;
    private final File screenshotDirectory;
    private final File temporaryImageFile;
    private final Consumer<Text> messageReceiver;
    
    private static final int GLOBAL_WIDGET_LENGTH = 150;
    private static final int GLOBAL_WIDGET_HEIGHT = 20;
    private static final int MARGIN = 5;
    private static final String TEMPORARY_FILE_NAME = "temp.png";
    
    public TextFieldWidget fileNameField = new TextFieldWidget(
            getClient().textRenderer,
            0, 0,
            GLOBAL_WIDGET_LENGTH * 2 / 3, GLOBAL_WIDGET_HEIGHT,
            literal("Screenshot File Name"));
    public final ButtonWidget saveButton = ButtonWidget.builder(
                    literal("Save"),
                    button -> saveToFile())
            .size(GLOBAL_WIDGET_LENGTH / 3, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(literal("Save the screenshot as a file")))
            .build();
    public final ButtonWidget copyClipboardButton = ButtonWidget.builder(
                    literal("Copy to Clipboard"),
                    this::copyToClipboard)
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(literal("Copy the image to the clipboard")))
            .build();
    public final ButtonWidget discardButton = ButtonWidget.builder(
                    literal("Discard Screenshot"),
                    button -> discardScreenshot())
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(literal("Discard the screenshot")))
            .build();
    
    public static void open(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        getClient().setScreen(new ScreenshotManagementScreen(screenshot, screenshotDirectory, messageReceiver));
    }
    
    public ScreenshotManagementScreen(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        super(literal("Screenshot Management"));
        
        this.screenshot = screenshot;
        this.screenshotDirectory = screenshotDirectory;
        this.messageReceiver = messageReceiver;
        this.temporaryImageFile = new File(screenshotDirectory, TEMPORARY_FILE_NAME);
        
        saveScreenshot(screenshot, temporaryImageFile);
    }
    
    @Override
    protected void init() {
        addRow(0, fileNameField, saveButton);
        addRow(1, copyClipboardButton);
        addRow(2, discardButton);
    }
    
    private void addRow(int row, ClickableWidget... widgets) {
        int x = MARGIN;
        final int y = MARGIN + GLOBAL_WIDGET_HEIGHT * row;
        
        for (ClickableWidget widget : widgets) {
            add(widget, x, y);
            x += widget.getWidth();
        }
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
        String saveName = getFileNameFieldValue() + ".png";
        boolean success = renameFile(temporaryImageFile, screenshotDirectory, saveName);
        
        if (success) sendMessage("Saved screenshot as " + saveName, GREEN);
        else         sendMessage("An error occurred while saving the screenshot.", RED);
        
        super.closeScreen();
    }
    
    private void copyToClipboard(ButtonWidget button) {
        boolean success = copyImageToClipboard(temporaryImageFile);
        
        if (success) sendMessage("Screenshot copied to clipboard!", GREEN);
        else         sendMessage("Failed to copy screenshot to clipboard.", RED);
        
        button.setMessage(literal("Copied!"));
        button.active = false;
    }
    
    private void discardScreenshot() {
        temporaryImageFile.delete();
        sendMessage("Screenshot discarded.", YELLOW);
        super.closeScreen();
    }
    
    private void sendMessage(String text, Formatting... formatting) {
        messageReceiver.accept(literal(text).formatted(formatting));
    }
    
    private String getFileNameFieldValue() {
        return fileNameField.getText();
    }
}
