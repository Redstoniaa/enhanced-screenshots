package snap.utils.config;

public class SnapSettings {
    public UiType uiMode;
    public String overridePath;
    
    public SnapSettings(UiType uiMode, String overridePath) {
        this.uiMode = uiMode;
        this.overridePath = overridePath;
    }
    
    public static SnapSettings getDefault() {
        return new SnapSettings(UiType.SCREEN, "egg");
    }
    
    public enum UiType {
        SCREEN,
        CHAT
    }
}
