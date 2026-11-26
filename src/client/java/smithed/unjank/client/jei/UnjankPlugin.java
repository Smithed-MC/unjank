package smithed.unjank.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import smithed.unjank.Unjank;

@JeiPlugin
public class UnjankPlugin implements IModPlugin {
    @Override
    public @NonNull Identifier getPluginUid() {
        return Unjank.id("jei_plugin");
    }
}
