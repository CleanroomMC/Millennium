package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.tag.ITaggable;
import com.cleanroommc.millennium.common.tag.Tag;
import com.cleanroommc.millennium.common.tag.TagHelpers;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(Item.class)
public abstract class ItemMixin implements ITaggable<Item> {

    @Shadow public abstract void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items);

    private NonNullList<ItemStack> mil$getAllItemStacks() {
        NonNullList<ItemStack> items = NonNullList.create();
        getSubItems(CreativeTabs.SEARCH, items);
        return items;
    }
    @Override
    public void addTag(Tag tag) {
        for(ItemStack stack : mil$getAllItemStacks()) {
            ITaggable.of(stack).addTag(tag);
        }
    }

    @Override
    public boolean isTag(Tag tag) {
        for(ItemStack stack : mil$getAllItemStacks()) {
            if(!ITaggable.of(stack).isTag(tag))
                return false;
        }
        return true;
    }

    @Override
    public void removeTag(Tag tag) {
        for(ItemStack stack : mil$getAllItemStacks()) {
            ITaggable.of(stack).removeTag(tag);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<Tag> getTags() {
        return TagHelpers.streamFromSubObjects((ImmutableList<ITaggable<ItemStack>>)(Object)mil$getAllItemStacks());
    }

    @Override
    public Class<Item> getTagDelegateType() {
        return Item.class;
    }
}
