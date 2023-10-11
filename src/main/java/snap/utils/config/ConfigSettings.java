package snap.utils.config;

public class ConfigSettings {
    public UiType uiType;
    
    public ConfigSettings(UiType uiType) {
        this.uiType = uiType;
    }
    
    public static ConfigSettings getDefault() {
        return new ConfigSettings(UiType.SCREEN);
    }
    
    public enum UiType {
        SCREEN,
        CHAT
    }
}
