package enhanced_screenshots;

import enhanced_screenshots.utils.translations.Translations;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedScreenshotsMod {
	public static final String MOD_ID = "enhanced-screenshots";
	public static ModMetadata METADATA;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static void init() {
		System.setProperty("java.awt.headless", "false");
		Translations.loadAllTranslations();
		getModMetadata();
	}
	
	private static void getModMetadata() {
		METADATA = FabricLoader.getInstance()
				.getModContainer(MOD_ID).orElseThrow()
				.getMetadata();
	}
}
