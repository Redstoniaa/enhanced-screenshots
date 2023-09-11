package enhanced_screenshots.utils;

import net.minecraft.text.MutableText;

public class Text {
    public static MutableText translated(String translationKey, Object... args) {
        return net.minecraft.text.Text.literal(
                Translations.translate(translationKey, args));
    }
}
