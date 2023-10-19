package snap.utils.config;

import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import snap.utils.io.JSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static snap.SnapMod.LOGGER;
import static snap.SnapMod.MOD_ID;

public class Config {
    public static SnapSettings currentSettings;
    private static final Path CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir();
    private static final Path CONFIG_PATH = CONFIG_DIRECTORY.resolve(MOD_ID + ".json");
    
    public static void readSettings() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                SnapRawSettings rawSettings = JSON.readFromPath(CONFIG_PATH, new TypeToken<>(){});
                currentSettings = rawSettings.processed();
            } catch (IOException exception) {
                LOGGER.error("An IO exception occurred trying to read the config file.", exception);
                saveAndUseDefaultSettings();
            }
        } else {
            saveAndUseDefaultSettings();
        }
    }
    
    public static void saveSettings() {
        try {
            JSON.writeToPath(currentSettings.raw(), CONFIG_PATH);
        } catch (IOException exception) {
            LOGGER.error("An IO exception occurred trying to save to the config file.", exception);
        }
    }
    
    private static void saveAndUseDefaultSettings() {
        currentSettings = SnapSettings.getDefault();
        saveSettings();
    }
}
