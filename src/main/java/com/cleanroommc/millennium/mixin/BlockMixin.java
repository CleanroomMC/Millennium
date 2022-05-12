package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.ITaggable;
import com.cleanroommc.millennium.common.tag.Tag;
import com.cleanroommc.millennium.common.tag.TagHelpers;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(Block.class)
public abstract class BlockMixin implements ITaggable<Block> {
    @Shadow public abstract BlockStateContainer getBlockState();

    /**
     * Add a tag for each of the block's states.
     */
    @Override
    public void addTag(Tag tag) {
        for(IBlockState state : getBlockState().getValidStates()) {
            ((ITaggable<IBlockState>)state).addTag(tag);
        }
    }

    /**
     * A block only has a tag if all of its states do.
     */
    @Override
    public boolean isTag(Tag tag) {
        for(IBlockState state : getBlockState().getValidStates()) {
            if(!((ITaggable<IBlockState>)state).isTag(tag))
                return false;
        }
        return true;
    }

    /**
     * Remove a tag from each of the block's states.
     */
    @Override
    public void removeTag(Tag tag) {
        for(IBlockState state : getBlockState().getValidStates()) {
            ((ITaggable<IBlockState>)state).removeTag(tag);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<Tag> getTags() {
        return TagHelpers.streamFromSubObjects((ImmutableList<ITaggable<IBlockState>>)(Object)getBlockState().getValidStates());
    }

    @Override
    public Class<Block> getTagDelegateType() {
        return Block.class;
    }
}
