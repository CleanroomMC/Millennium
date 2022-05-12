package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.ITaggable;
import com.cleanroommc.millennium.common.tag.Tag;
import com.cleanroommc.millennium.common.tag.TagDelegate;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(IForgeRegistryEntry.Impl.class)
public abstract class ForgeRegistryEntryImplMixin<T extends IForgeRegistryEntry<T>> implements ITaggable<T> {
    @Shadow public abstract Class<T> getRegistryType();

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
    public Class<T> getTagDelegateType() {
        return this.getRegistryType();
    }
}
