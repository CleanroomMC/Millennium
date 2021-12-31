package io.github.cleanroommc.millennium.bundle.sound;

import io.github.cleanroommc.millennium.Millennium;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BundleSoundEvents {

    public static SoundEvent REMOVE_ONE, DROP_CONTENTS, INSERT;

    public static void init(IForgeRegistry<SoundEvent> registry) {
        REMOVE_ONE = new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.remove_one"));
        DROP_CONTENTS = new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.drop_contents"));
        INSERT = new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.insert"));
        registry.registerAll(REMOVE_ONE, DROP_CONTENTS, INSERT);
    }

}
