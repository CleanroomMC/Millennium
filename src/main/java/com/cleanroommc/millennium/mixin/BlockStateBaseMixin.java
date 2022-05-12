package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.ITaggable;
import com.cleanroommc.millennium.common.tag.Tag;
import com.cleanroommc.millennium.common.tag.TagDelegate;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;

@Mixin(BlockStateBase.class)
public class BlockStateBaseMixin implements ITaggable<IBlockState> {
    @Override
    public void addTag(Tag tag) {
        TagDelegate.getDelegate(this).addTag(tag);
    }

    @Override
    public boolean isTag(Tag tag) {
        return TagDelegate.getDelegate(this).isTag(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        TagDelegate.getDelegate(this).removeTag(tag);
    }

    @Override
    public Stream<Tag> getTags() {
        return TagDelegate.getDelegate(this).getTags();
    }

    @Override
    public Class<IBlockState> getTagDelegateType() {
        return IBlockState.class;
    }
}
