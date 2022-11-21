package com.cleanroommc.millennium.content.v1_13;

import net.minecraft.item.EnumAction;
import net.minecraftforge.common.util.EnumHelper;

public class Version1_13 { // implements VersionModule

    public static EnumAction TRIDENT_ACTION;

    public static void initTridentAction() {
        TRIDENT_ACTION = EnumHelper.addAction("TRIDENT");
    }

}
