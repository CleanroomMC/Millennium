package com.cleanroommc.millennium.client.sounds;

import com.cleanroommc.millennium.Millennium;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BarrelSoundEvents {
    public static SoundEvent OPEN, CLOSE;

    public static void init(IForgeRegistry<SoundEvent> registry) {
        OPEN = new SoundEvent(new ResourceLocation(Millennium.MODID, "block.barrel.open"));
        OPEN.setRegistryName(OPEN.getSoundName());
        CLOSE = new SoundEvent(new ResourceLocation(Millennium.MODID, "block.barrel.close"));
        CLOSE.setRegistryName(CLOSE.getSoundName());
        registry.registerAll(OPEN, CLOSE);
    }
}
