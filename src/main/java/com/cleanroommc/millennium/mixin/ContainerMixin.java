package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.client.sounds.BundleSoundEvents;
import com.cleanroommc.millennium.common.items.BundleItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Container.class)
public class ContainerMixin {

    @Shadow public List<Slot> inventorySlots;

    @Inject(method = "slotClick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/InventoryPlayer;getItemStack()Lnet/minecraft/item/ItemStack;", ordinal = 8), cancellable = true)
    private void injectBundleCheck(int slotId, int dragType, ClickType clickType, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack playerStack = player.inventory.getItemStack();
        Slot slot = this.inventorySlots.get(slotId);
        if (dragType == 1 && playerStack.getItem() instanceof BundleItem) {
            ItemStack slotStack = slot.getStack();
            if (slotStack.isEmpty()) {
                ItemStack removedStack = BundleItem.removeOne(playerStack);
                if (!removedStack.isEmpty()) {
                    player.world.playSound(
                            null,
                            player.posX,
                            player.posY,
                            player.posZ,
                            BundleSoundEvents.REMOVE_ONE,
                            SoundCategory.PLAYERS,
                            0.8F,
                            (float) (0.8 + player.world.rand.nextFloat() * 0.4F));
                    slot.putStack(removedStack);
                    BundleItem.addOne(playerStack, slot.getStack());
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            }
        }
        ItemStack slotStack = this.inventorySlots.get(slotId).getStack();
        if (dragType == 0 && slotStack.getItem() instanceof BundleItem) {
            if (!playerStack.isEmpty()) {
                int remainder = BundleItem.addOne(slotStack, playerStack);
                player.world.playSound(
                        null,
                        player.posX,
                        player.posY,
                        player.posZ,
                        BundleSoundEvents.INSERT,
                        SoundCategory.PLAYERS,
                        0.8F,
                        (float) (0.8 + player.world.rand.nextFloat() * 0.4F));
                if (remainder > 0) {
                    playerStack.shrink(remainder);
                }
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }

}
