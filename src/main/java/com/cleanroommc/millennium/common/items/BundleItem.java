package com.cleanroommc.millennium.common.items;

import com.cleanroommc.millennium.client.sounds.BundleSoundEvents;
import com.google.common.collect.Streams;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class BundleItem extends MillenniumItem {
  private static final IItemPropertyGetter FULLNESS_PROPERTY =
      (stack, world, entity) -> (float) getContentWeight(stack) / 64F;

  public static final int MAX_WEIGHT = 64;

  private static final String TAG = "Items";
  private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;

  BundleItem() {
    super(new Settings().maxDamage(1).creativeTab(CreativeTabs.TOOLS).translationKey("bundle"));
  }

  private static int getWeight(ItemStack stack) {
    if (stack.getItem() instanceof BundleItem) {
      return 4 + getContentWeight(stack);
    }
    return 64 / stack.getMaxStackSize();
  }

  private static int getContentWeight(ItemStack stack) {
    return getContents(stack).mapToInt(content -> getWeight(content) * content.getCount()).sum();
  }

  public static ItemStack removeOne(ItemStack stack) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null) {
      if (tag.hasKey(TAG)) {
        NBTTagList list = tag.getTagList(TAG, 10);
        ItemStack removedStack = new ItemStack((NBTTagCompound) list.removeTag(0));
        if (list.isEmpty()) {
          tag.removeTag(TAG);
        }
        return removedStack;
      }
    }
    return ItemStack.EMPTY;
  }

  public static int addOne(ItemStack bundle, ItemStack add) {
    if (!add.isEmpty()) {
      int currentWeight = getContentWeight(bundle);
      int addWeight = getWeight(add);
      int remainingSize = Math.min(add.getCount(), (64 - currentWeight) / addWeight);
      if (remainingSize != 0) {
        NBTTagCompound tag = bundle.getTagCompound();
        if (tag == null) {
          tag = new NBTTagCompound();
          bundle.setTagCompound(tag);
        }
        NBTTagList list;
        if (tag.hasKey(TAG)) {
          list = tag.getTagList(TAG, 10);
        } else {
          list = new NBTTagList();
          tag.setTag(TAG, list);
        }
        NBTTagCompound matchingTag = getAndRemoveMatchingItem(add, list);
        if (matchingTag != null) {
          ItemStack matchingStack = new ItemStack(matchingTag);
          matchingStack.grow(remainingSize);
          matchingStack.writeToNBT(matchingTag);
          list.set(0, matchingTag);
        } else {
          ItemStack copyAdd = add.copy();
          copyAdd.setCount(remainingSize);
          NBTTagCompound newTag = new NBTTagCompound();
          copyAdd.writeToNBT(newTag);
          list.appendTag(newTag);
        }
        return remainingSize;
      }
    }
    return 0;
  }

  private static boolean dropContents(ItemStack stack, EntityPlayer player) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null) {
      if (tag.hasKey(TAG)) {
        if (player instanceof EntityPlayerMP) {
          NBTTagList list = tag.getTagList(TAG, 10);
          for (NBTBase itemTag : list) {
            if (itemTag instanceof NBTTagCompound) {
              ForgeHooks.onPlayerTossEvent(player, new ItemStack((NBTTagCompound) itemTag), true);
            }
          }
        }
        tag.removeTag(TAG);
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("UnstableApiUsage")
  private static Stream<ItemStack> getContents(ItemStack stack) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null) {
      if (tag.hasKey(TAG)) {
        NBTTagList list = tag.getTagList(TAG, 10);
        return Streams.stream(list).map(NBTTagCompound.class::cast).map(ItemStack::new);
      }
    }
    return Stream.of();
  }

  @Nullable
  private static NBTTagCompound getAndRemoveMatchingItem(ItemStack add, NBTTagList list) {
    if (!(add.getItem() instanceof BundleItem)) {
      Iterator<NBTBase> iter = list.iterator();
      while (iter.hasNext()) {
        NBTBase base = iter.next();
        if (base instanceof NBTTagCompound) {
          NBTTagCompound tag = (NBTTagCompound) base;
          ItemStack compareStack = new ItemStack(tag);
          if (ItemStack.areItemsEqual(compareStack, add)
              && ItemStack.areItemStackTagsEqual(compareStack, add)) {
            iter.remove();
            return tag;
          }
        }
      }
    }
    return null;
  }

  @Override
  public EnumActionResult onItemUse(
      EntityPlayer player,
      World world,
      BlockPos pos,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (dropContents(stack, player)) {
      world.playSound(
          null,
          player.posX,
          player.posY,
          player.posZ,
          BundleSoundEvents.DROP_CONTENTS,
          SoundCategory.PLAYERS,
          0.8F,
          (float) (0.8 + world.rand.nextFloat() * 0.4F));
      player.addStat(StatList.getObjectUseStats(this));
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.FAIL;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    getContents(stack).forEach(s -> tooltip.add(s.getDisplayName() + " x" + s.getMaxStackSize()));
  }
}
