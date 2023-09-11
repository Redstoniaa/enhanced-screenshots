package enhanced_screenshots.ui.management;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.util.function.Consumer;

import static enhanced_screenshots.ui.management.ScreenshotManagementHelper.*;
import static enhanced_screenshots.utils.Text.translated;
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
            translated("enhanced_screenshots.screen.file_name.label"));
    public final ButtonWidget saveButton = ButtonWidget.builder(
                    translated("enhanced_screenshots.screen.save.text"),
                    button -> saveToFile())
            .size(GLOBAL_WIDGET_LENGTH / 3, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("enhanced_screenshots.screen.save.tooltip")))
            .build();
    public final ButtonWidget copyClipboardButton = ButtonWidget.builder(
                    translated("enhanced_screenshots.screen.copy.text"),
                    this::copyToClipboard)
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("enhanced_screenshots.screen.copy.tooltip")))
            .build();
    public final ButtonWidget discardButton = ButtonWidget.builder(
                    translated("enhanced_screenshots.screen.discard.text"),
                    button -> discardScreenshot())
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("enhanced_screenshots.screen.discard.tooltip")))
            .build();
    
    public static void open(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        getClient().setScreen(new ScreenshotManagementScreen(screenshot, screenshotDirectory, messageReceiver));
    }
    
    public ScreenshotManagementScreen(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        super(translated("enhanced_screenshots.screen.name"));
        
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
            addWidget(widget, x, y);
            x += widget.getWidth();
        }
    }
    
    private void addWidget(ClickableWidget widget, int x, int y) {
        widget.setPosition(x, y);
        addDrawableChild(widget);
    }
    
    @Override
    public void closeScreen() {
        discardScreenshot();
    }
    
    private void saveToFile() {
        File destination = new File(screenshotDirectory, getFileNameFieldValue() + ".png");
        boolean success = renameFile(temporaryImageFile, destination);
    
        Text fileOpen = literal(destination.getName())
                .formatted(Formatting.UNDERLINE)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, destination.getAbsolutePath())));
        
        if (success) sendMessage(translated("enhanced_screenshots.screen.save.success").formatted(GREEN).append(fileOpen));
        else         sendMessage(translated("enhanced_screenshots.screen.save.failure").formatted(RED));
        
        super.closeScreen();
    }
    
    private void copyToClipboard(ButtonWidget button) {
        boolean success = copyImageToClipboard(temporaryImageFile);
    
        if (success) sendMessage(translated("enhanced_screenshots.screen.copy.success").formatted(GREEN));
        else         sendMessage(translated("enhanced_screenshots.screen.copy.failure").formatted(RED));
        
        button.setMessage(translated("enhanced_screenshots.screen.copy.text_success"));
        button.active = false;
    }
    
    private void discardScreenshot() {
        temporaryImageFile.delete();
        sendMessage(translated("enhanced_screenshots.screen.discard.success"));
        super.closeScreen();
    }
    
    private void sendMessage(MutableText text) {
        messageReceiver.accept(text);
    }
    
    private String getFileNameFieldValue() {
        return fileNameField.getText();
    }
}
