package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.ITaggable;
import com.cleanroommc.millennium.common.tag.MetaItem;
import com.cleanroommc.millennium.common.tag.Tag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ITaggable<ItemStack> {
    @Shadow public abstract Item getItem();

    @Shadow public abstract int getItemDamage();

    private MetaItem mil$toMetaItem() {
        return new MetaItem(this.getItem(), this.getItemDamage());
    }
    @Override
    public void addTag(Tag tag) {
        mil$toMetaItem().addTag(tag);
    }

    @Override
    public boolean isTag(Tag tag) {
        return mil$toMetaItem().isTag(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        mil$toMetaItem().removeTag(tag);
    }

    @Override
    public Stream<Tag> getTags() {
        return mil$toMetaItem().getTags();
    }

    @Override
    public Class<?> getTagDelegateType() {
        return ItemStack.class;
    }
}
