package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.TagEventHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OreDictionary.class)
public class OreDictionaryMixin {
    @Inject(method = "rebakeMap", at = @At("TAIL"), remap = false)
    private static void handleTags(CallbackInfo ci) {
        TagEventHandler.processExistingOredicts();
    }
}
