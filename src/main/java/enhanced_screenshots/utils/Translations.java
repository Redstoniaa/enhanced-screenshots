package enhanced_screenshots.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static enhanced_screenshots.EnhancedScreenshotsMod.LOGGER;

public class Translations {
    public static final ClassLoader CLASS_LOADER = Translations.class.getClassLoader();
    public static final Gson GSON = new Gson();
    
    public static final String LANGUAGES_PATH = "assets/enhanced_screenshots/lang/";
    
    public static Set<String> supportedLanguages;
    public static final String fallbackLanguage = "en_us";
    public static Map<String, Map<String, String>> translations = new HashMap<>();
    
    public static String translate(String translationKey, Object... args) {
        if (isLanguageSupported(getCurrentLanguage()))
            return translateTo(translationKey, getCurrentLanguage(), args);
        else if (isLanguageSupported(fallbackLanguage))
            return translateTo(translationKey, fallbackLanguage, args);
        return translationKey;
    }
    
    public static String translateTo(String translationKey, String language, Object... args) {
        return getTranslations(language)
                .get(translationKey)
                .formatted(args);
    }
    
    public static Map<String, String> getTranslations(String language) {
        return translations.get(language);
    }
    
    public static String getCurrentLanguage() {
        return MinecraftClient.getInstance()
                .getLanguageManager()
                .getLanguage();
    }
    
    public static boolean isLanguageSupported(String language) {
        return supportedLanguages.contains(language);
    }
    
    public static void loadTranslations() {
        try {
            loadSupportedLanguages();
        } catch (IOException e) {
            LOGGER.warn("Unable to get supported languages. Falling back on " + fallbackLanguage, e);
            supportedLanguages = Set.of(fallbackLanguage);
        }
        
        for (String languageCode : supportedLanguages) {
            try {
                Map<String, String> translation = loadTranslation(languageCode);
                translations.put(languageCode, translation);
            } catch (IOException e) {
                LOGGER.warn("Unable to read translations for " + languageCode, e);
                supportedLanguages.remove(languageCode);
            }
        }
    }
    
    private static void loadSupportedLanguages() throws IOException {
        String content = readResource(LANGUAGES_PATH + "meta/supported_languages.json");
        supportedLanguages = GSON.fromJson(content, new TypeToken<Set<String>>(){}.getType());
    }
    
    public static Map<String, String> loadTranslation(String languageCode) throws IOException {
        String content = readResource(LANGUAGES_PATH + "%s.json".formatted(languageCode));
        return GSON.fromJson(content, new TypeToken<Map<String, String>>(){}.getType());
    }
    
    private static String readResource(String resourcePath) throws IOException {
        InputStream supportedLanguagesStream = CLASS_LOADER.getResourceAsStream(resourcePath);
        if (supportedLanguagesStream == null)
            throw new IOException("Resource could not be found or accessed.");
        return IOUtils.toString(supportedLanguagesStream, StandardCharsets.UTF_8);
    }
}
