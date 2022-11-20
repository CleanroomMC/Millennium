package com.cleanroommc.millennium;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Millennium.MOD_ID, name = Millennium.NAME, version = Millennium.VERSION)
public class Millennium {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "millennium";
    public static final String NAME = "Millennium";
    public static final String VERSION = "1.0";

    // public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

}
