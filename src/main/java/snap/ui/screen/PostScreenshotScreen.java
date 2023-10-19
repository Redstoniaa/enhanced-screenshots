package snap.ui.screen;

import snap.Screenshot;
import snap.utils.io.IO;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
//#if MC>=12002
import net.minecraft.client.gui.widget.button.ButtonWidget;
//#else
//$$ import net.minecraft.client.gui.widget.ButtonWidget;
//#endif
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import static snap.utils.Text.translated;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class PostScreenshotScreen
        extends Screen {
    private final Screenshot screenshot;
    
    private boolean wasPreviousFileNameFieldBlank = true;
    
    private static final int GLOBAL_WIDGET_LENGTH = 150;
    private static final int GLOBAL_WIDGET_HEIGHT = 20;
    private static final int MARGIN = 5;
    
    public TextFieldWidget fileNameField = new TextFieldWidget(
            MinecraftClient.getInstance().textRenderer,
            0, 0,
            GLOBAL_WIDGET_LENGTH * 2 / 3, GLOBAL_WIDGET_HEIGHT,
            translated("snap.screen.file_name.label"));
    public final ButtonWidget renameButton = ButtonWidget.builder(
                    translated("snap.screen.rename.text_keep"),
                    button -> rename())
            .size(GLOBAL_WIDGET_LENGTH / 3, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("snap.screen.rename.tooltip")))
            .build();
    public final ButtonWidget copyButton = ButtonWidget.builder(
                    translated("snap.screen.copy.text"),
                    button -> copy())
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("snap.screen.copy.tooltip")))
            .build();
    public final ButtonWidget discardButton = ButtonWidget.builder(
                    translated("snap.screen.discard.text"),
                    button -> discard())
            .size(GLOBAL_WIDGET_LENGTH, GLOBAL_WIDGET_HEIGHT)
            .tooltip(Tooltip.create(translated("snap.screen.discard.tooltip")))
            .build();
    
    public static void open(Screenshot screenshot) {
        MinecraftClient.getInstance()
                .setScreen(new PostScreenshotScreen(screenshot));
    }
    
    public PostScreenshotScreen(Screenshot screenshot) {
        super(translated("snap.screen.name"));
        this.screenshot = screenshot;
        
        setInitialFocus(fileNameField);
        fileNameField.setChangedListener(this::onFileNameFieldChange);
        if (IO.isHeadless) {
            setWidgetActive(copyButton,
                            false,
                            translated("snap.screen.copy.text_unavailable"),
                            Tooltip.create(translated("snap.screen.copy.tooltip_unavailable")));
        }
    }
    
    @Override
    protected void init() {
        addRow(0, fileNameField, renameButton);
        addRow(1, copyButton);
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
        addDrawableSelectableElement(widget);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ENTER)
            rename();
        else if (hasControlDown() && keyCode == GLFW_KEY_C)
            copy();
        // ESC key already calls closeScreen() in super.keyPressed(), so no need to do it here.
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    public void onFileNameFieldChange(String content) {
        boolean isBlank = content.isBlank();
        if (wasPreviousFileNameFieldBlank == isBlank)
            return;
        wasPreviousFileNameFieldBlank = isBlank;
        
        if (isBlank) renameButton.setMessage(translated("snap.screen.rename.text_keep"));
        else         renameButton.setMessage(translated("snap.screen.rename.text"));
    }
    
    @Override
    public void closeScreen() {
        discard();
    }
    
    private void rename() {
        boolean success = screenshot.renameImageFile(getSpecifiedFileName());
        if (success) super.closeScreen();
    }
    
    private void copy() {
        boolean success = screenshot.copyImageToClipboard();
        if (success) {
            setWidgetActive(copyButton,
                            false,
                            translated("snap.screen.copy.text_success"));
        }
    }
    
    private void discard() {
        screenshot.deleteImage();
        super.closeScreen();
    }
    
    private void setWidgetActive(ClickableWidget widget, boolean active, Text text) {
        widget.active = active;
        widget.setMessage(text);
    }
    
    private void setWidgetActive(ClickableWidget widget, boolean active, Text text, Tooltip tooltip) {
        setWidgetActive(widget, active, text);
        widget.setTooltip(tooltip);
    }
    
    private String getSpecifiedFileName() {
        return fileNameField.getText();
    }
}
