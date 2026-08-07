package smithed.unjank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnjankConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("unjank.json");

    public boolean disableFocusBorder = true;
    public boolean disableWarningBox = true;
    public boolean disableCommandWarning = true;

    public boolean disableFocusBorder() {
        return disableFocusBorder;
    }

    public boolean disableWarningBox() {
        return disableWarningBox;
    }

    public boolean disableCommandWarning() {
        return disableCommandWarning;
    }

    public static UnjankConfig createAndLoad() {
        UnjankConfig config = new UnjankConfig();
        config.load();
        return config;
    }

    public void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                UnjankConfig loaded = GSON.fromJson(reader, UnjankConfig.class);
                if (loaded != null) {
                    this.disableFocusBorder = loaded.disableFocusBorder;
                    this.disableWarningBox = loaded.disableWarningBox;
                    this.disableCommandWarning = loaded.disableCommandWarning;
                }
            } catch (Exception e) {
                Unjank.LOGGER.error("Failed to load Unjank config, using defaults", e);
            }
        } else {
            save();
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            Unjank.LOGGER.error("Failed to save Unjank config", e);
        }
    }
}
