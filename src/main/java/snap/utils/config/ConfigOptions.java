package snap.utils.config;

public class ConfigOptions {
    public UiType uiType;
    
    public ConfigOptions(UiType uiType) {
        this.uiType = uiType;
    }
    
    public static ConfigOptions getDefault() {
        return new ConfigOptions(UiType.SCREEN);
    }
    
    public enum UiType {
        SCREEN,
        CHAT
    }
}
