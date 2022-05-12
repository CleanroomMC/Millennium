package com.cleanroommc.millennium.common.tag;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class TagEventHandler {

    private static final ArrayList<Tag> oreDictTags = new ArrayList<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void processExistingOredicts() {
        for(Tag tag : oreDictTags) {
            TagDelegate.removeFromAll(tag);
        }
        oreDictTags.clear();
        for(String oreName : OreDictionary.getOreNames()) {
            for(ItemStack stack : OreDictionary.getOres(oreName)) {
                processOredictEntry(stack, oreName);
            }
        }
    }

    public static void processOredictEntry(ItemStack stack, String name) {
        Item item = stack.getItem();
        int meta = stack.getItemDamage() == OreDictionary.WILDCARD_VALUE ? -1 : stack.getItemDamage();
        Tag tag = Tag.oredict(name);
        ITaggable.of(stack).addTag(tag);
        assert ITaggable.of(new ItemStack(item, 1, meta == -1 ? 0 : meta)).isTag(tag);
        oreDictTags.add(tag);
        if(item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).getBlock();
            /* This is really not ideal... but what else can one do? */
            for(IBlockState state : block.getBlockState().getValidStates()) {
                if(meta == -1 || meta == block.damageDropped(state)) {
                    ITaggable.of(state).addTag(tag);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderRight(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.world.getBlockState(blockpos);
            ITaggable.of(state).getTags().forEach(tag -> {
                event.getRight().add(tag.toString());
            });
        }
    }

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(event.getFlags().isAdvanced() && !stack.isEmpty()) {
            ITaggable.of(stack).getTags().forEach(tag -> {
                event.getToolTip().add(TextFormatting.DARK_GRAY + "" + tag.toString());
            });
        }
    }
}
