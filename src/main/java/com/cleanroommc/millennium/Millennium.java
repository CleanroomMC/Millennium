package com.cleanroommc.millennium;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.cleanroommc.millennium.content.v1_13.Version1_13;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Mod(modid = Millennium.MOD_ID, name = Millennium.NAME, version = Millennium.VERSION)
public class Millennium {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "millennium";
    public static final String NAME = "Millennium";
    public static final String VERSION = "1.0";

    // public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public Millennium() {
        moveAssets();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initModules();
    }

    public void initModules() {
        Version1_13.initTridentAction();
    }

    private void moveAssets() {
        Map<String, String> assetMap = new Object2ObjectOpenHashMap<>();
        assetMap.put("minecraft/textures/item/trident.png", "assets/millennium/textures/item/trident.png");
        AssetMoverAPI.fromMinecraft("1.19.2", assetMap);
    }

}
