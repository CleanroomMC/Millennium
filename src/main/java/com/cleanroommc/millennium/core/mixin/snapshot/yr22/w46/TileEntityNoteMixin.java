package com.cleanroommc.millennium.core.mixin.snapshot.yr22.w46;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityNote.class)
public class TileEntityNoteMixin {

    @Shadow public byte note;

    /**
     * @author Rongmario
     * @reason Supports mob sounds being played when there are mob heads on the block. TODO: support custom sounds with an id system(?)
     */
    @Overwrite
    public void triggerNote(World world, BlockPos pos) {
        BlockPos upPos = pos.up();
        IBlockState upState = world.getBlockState(upPos);
        if (upState.getMaterial() == Material.AIR) {
            IBlockState downState = world.getBlockState(pos.down());
            Material material = downState.getMaterial();
            int i = 0;
            if (material == Material.ROCK) {
                i = 1;
            } else if (material == Material.SAND) {
                i = 2;
            } else if (material == Material.GLASS) {
                i = 3;
            } else if (material == Material.WOOD) {
                i = 4;
            }
            Block block = downState.getBlock();
            if (block == Blocks.CLAY) {
                i = 5;
            } else if (block == Blocks.GOLD_BLOCK) {
                i = 6;
            } else if (block == Blocks.WOOL) {
                i = 7;
            } else if (block == Blocks.PACKED_ICE) {
                i = 8;
            } else if (block == Blocks.BONE_BLOCK) {
                i = 9;
            }
            world.addBlockEvent(pos, Blocks.NOTEBLOCK, i, this.note);
        } else if (upState.getBlock() instanceof BlockSkull) {
            TileEntity tile = world.getTileEntity(upPos);
            if (tile instanceof TileEntitySkull) {
                int skullType = ((TileEntitySkull) tile).getSkullType();
                if (skullType != 3) { // 3 == Player/Character
                    world.addBlockEvent(pos, Blocks.NOTEBLOCK, -(skullType + 1), this.note); // Negative IDs for mobs, for avoiding conflicts' sake.
                }
            }
        }
    }

}
