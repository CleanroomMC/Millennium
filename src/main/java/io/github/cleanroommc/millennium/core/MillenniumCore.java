package io.github.cleanroommc.millennium.core;

import io.github.cleanroommc.millennium.MillenniumConfig;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("Millennium|Core")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class MillenniumCore implements IFMLLoadingPlugin {

    public MillenniumCore() {
        MixinBootstrap.init();
        if (MillenniumConfig.INSTANCE.bundles$Enabled) {
            Mixins.addConfiguration("mixin.millennium.bundle.json");
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
