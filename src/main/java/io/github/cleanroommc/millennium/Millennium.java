package io.github.cleanroommc.millennium;

import io.github.cleanroommc.millennium.bundle.item.ItemBundle;
import io.github.cleanroommc.millennium.bundle.sound.BundleSoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Millennium.MODID, name = Millennium.NAME, version = Millennium.VERSION)
public class Millennium {

    public static final String MODID = "millennium";
    public static final String NAME = "Millennium";
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> event) {
        ItemBundle.init(event.getRegistry());
    }

    @SubscribeEvent
    public void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        BundleSoundEvents.init(event.getRegistry());
    }

}
