package snap.utils.config;

import org.jetbrains.annotations.Nullable;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static snap.SnapMod.LOGGER;

public class SnapRawSettings {
    public SnapSettings.UiType uiMode;
    public String screenshotDirectoryOverride;
    
    public SnapRawSettings(SnapSettings.UiType uiMode, String screenshotDirectoryOverride) {
        this.uiMode = uiMode;
        this.screenshotDirectoryOverride = screenshotDirectoryOverride;
    }
    
    public SnapSettings processed() {
        return new SnapSettings(uiMode,
                                getDirectoryOverrideAsPath());
    }
    
    @Nullable
    private Path getDirectoryOverrideAsPath() {
        if (screenshotDirectoryOverride.isBlank())
            return null;
        
        try {
            return Path.of(screenshotDirectoryOverride);
        } catch (InvalidPathException exception) {
            LOGGER.error("Provided path for screenshotDirectoryOverride is invalid! Defaulting to saving in ./minecraft/screenshots", exception);
            return null;
        }
    }
}
