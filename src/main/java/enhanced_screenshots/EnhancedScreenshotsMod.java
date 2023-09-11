package enhanced_screenshots;

import enhanced_screenshots.utils.Translations;
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
		Translations.loadTranslations();
		getModMetadata();
	}
	
	private static void getModMetadata() {
		METADATA = FabricLoader.getInstance()
				.getModContainer(MOD_ID).orElseThrow()
				.getMetadata();
	}
	
	public static boolean isInDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
	
	public static void logDevInfo(String info) {
		if (isInDevEnvironment()) {
			LOGGER.info(info);
		}
	}
}
