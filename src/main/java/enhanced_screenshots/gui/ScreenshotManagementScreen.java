package enhanced_screenshots.gui;

import com.mojang.blaze3d.texture.NativeImage;
import enhanced_screenshots.utils.file.Files;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.io.File;
import java.util.function.Consumer;

import static enhanced_screenshots.utils.Text.translated;
import static net.minecraft.text.Text.literal;
import static net.minecraft.util.Formatting.GREEN;
import static net.minecraft.util.Formatting.RED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class ScreenshotManagementScreen
        extends Screen {
    //    private final NativeImage screenshot;
    private final File screenshotDirectory;
    private final File unnamedScreenshotFile;
    
    private final Consumer<Text> messageReceiver;
//    private final Identifier previewId;
    
    private static final int GLOBAL_WIDGET_LENGTH = 150;
    private static final int GLOBAL_WIDGET_HEIGHT = 20;
    private static final int MARGIN = 5;
    
    public TextFieldWidget fileNameField = new TextFieldWidget(
            MinecraftClient.getInstance().textRenderer,
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
                    button -> copyToClipboard())
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
        MinecraftClient.getInstance()
                .setScreen(new ScreenshotManagementScreen(screenshot, screenshotDirectory, messageReceiver));
    }
    
    public ScreenshotManagementScreen(NativeImage screenshot, File screenshotDirectory, Consumer<Text> messageReceiver) {
        super(translated("enhanced_screenshots.screen.name"));

//        this.screenshot = screenshot;
        this.screenshotDirectory = screenshotDirectory;
        this.messageReceiver = messageReceiver;
        this.unnamedScreenshotFile = new File(screenshotDirectory, Util.getFileNameFormattedDateTime() + ".png");
//        this.previewId = MinecraftClient.getInstance()
//                .getTextureManager()
//                .registerDynamicTexture("enhanced_screenshots_preview",
//                                        new NativeImageBackedTexture(screenshot));
        
        Files.saveNativeImage(screenshot, unnamedScreenshotFile);
        setInitialFocus(fileNameField);
    }
    
    @Override
    protected void init() {
        addRow(0, fileNameField, saveButton);
        addRow(1, copyClipboardButton);
        addRow(2, discardButton);
    }

//    @Override
//    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
//        graphics.drawTexture(previewId,
//                             0, 0,
//                             screenshot.getWidth(), screenshot.getHeight(),
//                             0, 0,
//                             screenshot.getWidth(), screenshot.getHeight(),
//                             screenshot.getWidth(), screenshot.getHeight());
//        super.render(graphics, mouseX, mouseY, delta);
//    }
    
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ENTER)
            saveToFile();
        else if (hasControlDown() && keyCode == GLFW_KEY_C)
            copyToClipboard();
        // ESC key already calls closeScreen() in super.keyPressed(), so no need to do it here.
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void closeScreen() {
        discardScreenshot();
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
    
    private void saveToFile() {
        File destination;
        boolean success = true;
        if (!getSpecifiedFileName().isBlank()) {
            destination = new File(screenshotDirectory, getSpecifiedFileName() + ".png");
            success = Files.rename(unnamedScreenshotFile, destination);
        } else {
            destination = unnamedScreenshotFile;
        }
        
        Text openScreenshotClickable = literal(destination.getName())
                .formatted(Formatting.UNDERLINE)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, destination.getAbsolutePath())));
        
        if (success) sendMessage(translated("enhanced_screenshots.screen.save.success").formatted(GREEN).append(openScreenshotClickable));
        else         sendMessage(translated("enhanced_screenshots.screen.save.failure").formatted(RED));
        
        super.closeScreen();
    }
    
    private void copyToClipboard() {
        boolean success = Files.copyImageToClipboard(unnamedScreenshotFile);
        
        if (success) sendMessage(translated("enhanced_screenshots.screen.copy.success").formatted(GREEN));
        else         sendMessage(translated("enhanced_screenshots.screen.copy.failure").formatted(RED));
        
        copyClipboardButton.setMessage(translated("enhanced_screenshots.screen.copy.text_success"));
        copyClipboardButton.active = false;
    }
    
    private void discardScreenshot() {
        unnamedScreenshotFile.delete();
        sendMessage(translated("enhanced_screenshots.screen.discard.success"));
        super.closeScreen();
    }
    
    private void sendMessage(Text text) {
        messageReceiver.accept(text);
    }
    
    private String getSpecifiedFileName() {
        return fileNameField.getText();
    }
}
