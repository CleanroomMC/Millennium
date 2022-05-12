package com.cleanroommc.millennium.common.tag;

import net.minecraft.item.Item;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Proxy class, to avoid storing lots of ItemStack instances in memory.
 */
public class MetaItem implements ITaggable<MetaItem> {
    private final Item item;
    private final int meta;

    public MetaItem(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof MetaItem))
            return false;
        MetaItem mi = (MetaItem)o;
        return mi.item.equals(item) && mi.meta == meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, meta);
    }

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
    public Class<?> getTagDelegateType() {
        return MetaItem.class;
    }

    @Override
    public String toString() {
        return "MetaItem[item=" + item.getRegistryName() + ",meta=" + meta + "]";
    }
}
