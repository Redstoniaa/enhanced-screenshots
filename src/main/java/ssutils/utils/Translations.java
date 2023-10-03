package ssutils.utils;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ssutils.ScreenshotUtilitiesMod.LOGGER;

public class Translations {
    public static Map<String, Map<String, String>> translations = new HashMap<>();
    public static Set<String> supportedLanguages;
    public static final String FALLBACK_LANGUAGE = "en_us";
    
    private static final String LANGUAGES_DIRECTORY = "assets/ssutils/lang/";
    private static final String SUPPORTED_LANGUAGES_PATH = LANGUAGES_DIRECTORY + "meta/supported_languages.json";
    private static final String LANGUAGE_PATH = LANGUAGES_DIRECTORY + "%s.json";
    
    private static final ClassLoader CLASS_LOADER = Translations.class.getClassLoader();
    private static final Gson GSON = new Gson();
    
    public static String translate(String key, Object... args) {
        if (isLanguageSupported(getCurrentLanguage()))
            return translateTo(getCurrentLanguage(), key, args);
        else if (isLanguageSupported(FALLBACK_LANGUAGE))
            return translateTo(FALLBACK_LANGUAGE, key, args);
        return key;
    }
    
    public static String translateTo(String language, String key, Object... args) {
        if (hasTranslationFor(language, key))
            return getTranslations(language)
                    .get(key)
                    .formatted(args);
        return key;
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
    
    public static boolean hasTranslationFor(String language, String key) {
        return isLanguageSupported(language) && getTranslations(language).containsKey(key);
    }
    
    public static void loadAllTranslations() {
        try {
            supportedLanguages = readSupportedLanguages();
        } catch (IOException e) {
            LOGGER.warn("Failed to get supported languages. Falling back on " + FALLBACK_LANGUAGE, e);
            supportedLanguages = Sets.newHashSet(FALLBACK_LANGUAGE);
        }
        
        for (String languageCode : supportedLanguages) {
            try {
                translations.put(languageCode, readTranslation(languageCode));
            } catch (IOException e) {
                LOGGER.warn("Failed to read translations for " + languageCode, e);
                supportedLanguages.remove(languageCode);
            }
        }
    }
    
    private static Set<String> readSupportedLanguages() throws IOException {
        return readAndInterpretResourceJson(SUPPORTED_LANGUAGES_PATH, new TypeToken<>(){});
    }
    
    private static Map<String, String> readTranslation(String languageCode) throws IOException {
        return readAndInterpretResourceJson(LANGUAGE_PATH.formatted(languageCode), new TypeToken<>(){});
    }
    
    private static <T> T readAndInterpretResourceJson(String resourcePath, TypeToken<T> typeToken) throws IOException {
        return GSON.fromJson(readResource(resourcePath), typeToken);
    }
    
    private static String readResource(String resourcePath) throws IOException {
        InputStream supportedLanguagesStream = CLASS_LOADER.getResourceAsStream(resourcePath);
        if (supportedLanguagesStream == null)
            throw new IOException("Resource could not be found or accessed.");
        return IOUtils.toString(supportedLanguagesStream, StandardCharsets.UTF_8);
    }
}
