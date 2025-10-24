package smithed.unjank;


import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "unjank")
@Config(name = "unjank", wrapperName = "UnjankConfig")
public class UnjankConfigModel {
    public boolean disableFocusBorder = true;
    public boolean disableWarningBox = true;
    public boolean disableCommandWarning = true;
}