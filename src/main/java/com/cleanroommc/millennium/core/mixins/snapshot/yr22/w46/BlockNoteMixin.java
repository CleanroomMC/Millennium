package com.cleanroommc.millennium.core.mixins.snapshot.yr22.w46;

import com.cleanroommc.millennium.core.mixins.expanders.ItemSkullAccessor;
import net.minecraft.block.BlockNote;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockNote.class)
public class BlockNoteMixin {

    @Inject(method = "eventReceived", at = @At("HEAD"), cancellable = true)
    private void captureMobSoundEvent(IBlockState state, World world, BlockPos pos, int id, int param, CallbackInfoReturnable<Boolean> cir) {
        if (id < 0) { // Mob Sound, TODO: make it extensive!
            String mobType = ItemSkullAccessor.getSkullTypes()[Math.abs(id) - 1];
            SoundEvent event = null;
            switch (mobType) {
                case "skeleton":
                    event = SoundEvents.ENTITY_SKELETON_AMBIENT;
                    break;
                case "wither":
                    event = SoundEvents.ENTITY_WITHER_AMBIENT;
                    break;
                case "zombie":
                    event = SoundEvents.ENTITY_ZOMBIE_AMBIENT;
                    break;
                case "creeper":
                    event = SoundEvents.ENTITY_CREEPER_PRIMED;
                    break;
                case "dragon":
                    event = SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
                    break;
            }
            if (event != null) {
                world.playSound(null, pos, event, SoundCategory.RECORDS, 3.0F, 1.0F);
                cir.setReturnValue(true);
            }
        }
    }

}
