package com.cleanroommc.millennium.common.tag;

import com.cleanroommc.millennium.Millennium;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Tags can be used to categorize registry entries. Some registry entries (like blocks and items) have special logic
 * to allow you to put a tag on all meta values, otherwise they are just applied to the registry entry directly.
 *
 * You can access the tagging facilities of registry entries using {@link ITaggable#of(Object)}.
 */
public final class Tag extends ResourceLocation {
    private static final Interner<Tag> INTERNER = Interners.newStrongInterner();
    Tag(String namespaceIn, String pathIn) {
        super(namespaceIn, pathIn);
    }

    @Override
    public int hashCode() {
        return 38 * this.namespace.hashCode() * this.path.hashCode();
    }

    @Override
    public String toString()
    {
        return '#' + this.namespace + ':' + this.path;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        return super.equals(other) && (other instanceof Tag);
    }

    public static Tag of(@Nonnull ResourceLocation tagId) {
        return (Tag)INTERNER.intern(new Tag(tagId.getNamespace(), tagId.getPath()));
    }

    public static Tag oredict(@Nonnull String oreName) {
        String reformattedName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, oreName);
        return of(new ResourceLocation(Millennium.MODID, "oredict/" + reformattedName));
    }
}
