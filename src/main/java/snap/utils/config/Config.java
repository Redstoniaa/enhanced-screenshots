package snap.utils.config;

import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import snap.utils.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static snap.SnapMod.LOGGER;
import static snap.SnapMod.MOD_ID;

public class Config {
    public static ConfigSettings currentSettings;
    private static final Path CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir();
    private static final Path CONFIG_PATH = CONFIG_DIRECTORY.resolve(MOD_ID + ".json");
    
    public static void readConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                currentSettings = IO.readJsonFromPath(CONFIG_PATH, new TypeToken<ConfigSettings>(){});
            } catch (IOException exception) {
                LOGGER.error("An IO exception occurred trying to read the config file.", exception);
                currentSettings = ConfigSettings.getDefault();
            }
        } else {
            currentSettings = ConfigSettings.getDefault();
        }
    }
    
    public static void saveConfig() {
        try {
            IO.writeJsonToFile(currentSettings, CONFIG_PATH);
        } catch (IOException exception) {
            LOGGER.error("An IO exception occurred trying to save to the config file.", exception);
        }
    }
}
