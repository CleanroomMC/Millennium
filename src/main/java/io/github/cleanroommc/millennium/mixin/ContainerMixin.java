package io.github.cleanroommc.millennium.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public class ContainerMixin {

    @Inject(method = "slotClick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/InventoryPlayer;getItemStack()Lnet/minecraft/item/ItemStack;", ordinal = 8))
    private void injectBundleCheck(int slotId, int dragType, ClickType j3, EntityPlayer k3, CallbackInfoReturnable<ItemStack> cir) {

    }

}
