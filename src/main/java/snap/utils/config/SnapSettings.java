package snap.utils.config;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class SnapSettings {
    public UiType uiMode;
    @Nullable
    public Path directoryOverride;
    
    public SnapSettings(UiType uiMode, @Nullable Path directoryOverride) {
        this.uiMode = uiMode;
        this.directoryOverride = directoryOverride;
    }
    
    public SnapRawSettings raw() {
        return new SnapRawSettings(uiMode,
                                   getDirectoryOverrideAsString());
    }
    
    private String getDirectoryOverrideAsString() {
        return directoryOverride != null
                ? directoryOverride.toAbsolutePath().toString()
                : "";
    }
    
    public static SnapSettings getDefault() {
        return new SnapSettings(UiType.SCREEN,
                                null);
    }
    
    public enum UiType {
        SCREEN,
        CHAT
    }
}
