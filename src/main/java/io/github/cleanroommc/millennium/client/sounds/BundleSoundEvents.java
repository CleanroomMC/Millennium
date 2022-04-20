package io.github.cleanroommc.millennium.client.sounds;

import io.github.cleanroommc.millennium.Millennium;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BundleSoundEvents {
  public static SoundEvent REMOVE_ONE, DROP_CONTENTS, INSERT;

  public static void init(IForgeRegistry<SoundEvent> registry) {
    REMOVE_ONE = new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.remove_one"));
    REMOVE_ONE.setRegistryName(REMOVE_ONE.getSoundName());
    DROP_CONTENTS =
        new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.drop_contents"));
    DROP_CONTENTS.setRegistryName(DROP_CONTENTS.getSoundName());
    INSERT = new SoundEvent(new ResourceLocation(Millennium.MODID, "item.bundle.insert"));
    INSERT.setRegistryName(INSERT.getSoundName());
    registry.registerAll(REMOVE_ONE, DROP_CONTENTS, INSERT);
  }
}
