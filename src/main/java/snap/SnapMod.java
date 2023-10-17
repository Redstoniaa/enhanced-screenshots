package snap;

import snap.utils.Translations;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snap.utils.config.Config;

public class SnapMod {
    public static final String MOD_ID = "snap";
    public static ModMetadata METADATA;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    public static void init() {
        System.setProperty("java.awt.headless", "false");
        Translations.loadAllTranslations();
        Config.readSettings();
        getModMetadata();
    }
    
    private static void getModMetadata() {
        METADATA = FabricLoader.getInstance()
                .getModContainer(MOD_ID).orElseThrow()
                .getMetadata();
    }
}
