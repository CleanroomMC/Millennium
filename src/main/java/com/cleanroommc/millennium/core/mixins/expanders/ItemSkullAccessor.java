package com.cleanroommc.millennium.core.mixins.expanders;

import net.minecraft.item.ItemSkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemSkull.class)
public interface ItemSkullAccessor {

    @Accessor(value = "SKULL_TYPES")
    static String[] getSkullTypes() {
        throw new AssertionError();
    }

}
