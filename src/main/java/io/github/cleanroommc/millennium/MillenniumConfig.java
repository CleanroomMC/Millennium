package io.github.cleanroommc.millennium;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class MillenniumConfig {

    public static final MillenniumConfig INSTANCE = new MillenniumConfig();

    static {
        INSTANCE.initialize();
    }

    private Configuration configuration;

    public boolean bundles$Enabled;

    public void initialize() {
        configuration = new Configuration(new File(Launch.minecraftHome, "config" + File.separator + "millennium.cfg"));
        load();
    }

    private void load() {
        bundles$Enabled = getCoreBoolean("enabled", "bundles", "Enable bundles module?", true);
    }

    private boolean getCoreBoolean(String name, String category, String description, boolean defaultValue) {
        Property prop = configuration.get(category, name, defaultValue);
        prop.setDefaultValue(defaultValue);
        prop.setComment(description + " - <default: " + defaultValue + ">");
        prop.setRequiresMcRestart(true);
        prop.setShowInGui(false);
        prop.setLanguageKey("millennium.config." + name);
        return prop.getBoolean(defaultValue);
    }

}
