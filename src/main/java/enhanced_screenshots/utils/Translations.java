package enhanced_screenshots.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static enhanced_screenshots.EnhancedScreenshotsMod.LOGGER;

public class Translations {
    public static final ClassLoader classLoader = Translations.class.getClassLoader();
    public static final Gson gson = new Gson();
    
    public static final String languagesPath = "assets/enhanced_screenshots/lang/";
    
    public static Set<String> supportedLanguages;
    public static final String fallbackLanguage = "en_us";
    public static Map<String, Map<String, String>> translations = new HashMap<>();
    
    public static void loadTranslations() {
        try {
            getSupportedLanguages();
        } catch (IOException e) {
            LOGGER.warn("Was unable to read supported languages file.", e);
            supportedLanguages = Set.of(fallbackLanguage);
        }
        
        for (String languageCode : supportedLanguages) {
            try {
                Map<String, String> translation = getTranslation(languageCode);
                translations.put(languageCode, translation);
            } catch (IOException e) {
                LOGGER.warn("Was unable to read translations for " + languageCode, e);
            }
        }
    }
    
    private static void getSupportedLanguages() throws IOException {
        String content = read(languagesPath + "meta/supported_languages.json");
        supportedLanguages = gson.fromJson(content, new TypeToken<Set<String>>(){}.getType());
    }
    
    public static Map<String, String> getTranslation(String languageCode) throws IOException {
        if (!supportedLanguages.contains(languageCode))
            return Collections.emptyMap();
        String content = read(languagesPath + "%s.json".formatted(languageCode));
        return gson.fromJson(content, new TypeToken<Map<String, String>>(){}.getType());
    }
    
    private static String read(String resourcePath) throws IOException {
        InputStream supportedLanguagesStream = classLoader.getResourceAsStream(resourcePath);
        if (supportedLanguagesStream == null)
            throw new IOException("Fail");
        return IOUtils.toString(supportedLanguagesStream, StandardCharsets.UTF_8);
    }
    
    public static String translate(String translationKey, Object... args) {
        if (isSupported(getCurrentLanguageCode()))
            return translateFromLanguage(translationKey, getCurrentLanguageCode(), args);
        else if (isSupported(fallbackLanguage))
            return translateFromLanguage(translationKey, fallbackLanguage, args);
        return translationKey;
    }
    
    public static String translateFromLanguage(String translationKey, String languageCode, Object... args) {
        return translations.get(languageCode).get(translationKey).formatted(args);
    }
    
    public static String getCurrentLanguageCode() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage();
    }
    
    public static boolean isSupported(String languageCode) {
        return supportedLanguages.contains(languageCode);
    }
}
