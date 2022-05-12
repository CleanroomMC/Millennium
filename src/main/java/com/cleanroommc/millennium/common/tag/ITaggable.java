package com.cleanroommc.millennium.common.tag;

import java.util.stream.Stream;

/**
 * Represents an object that can be tagged. All Forge registry entries have this interface on them. It is also available
 * on ItemStacks and IBlockStates.
 */
public interface ITaggable<T> {
    /**
     * Add a tag to an object.
     * @param tag
     */
    void addTag(Tag tag);

    /**
     * Check if this ITaggable has a certain tag.
     * @param tag
     * @return
     */
    boolean isTag(Tag tag);

    /**
     * Remove a tag from this object.
     * @param tag
     */
    void removeTag(Tag tag);

    /**
     * Get the list of tags this object contains.
     * @return
     */
    Stream<Tag> getTags();
    Class<?> getTagDelegateType();

    /**
     * Retrieves the ITaggable or throw an exception if it can't be tagged.
     * @param object The object to convert
     */
    @SuppressWarnings("unchecked")
    static <T> ITaggable<T> of(T object) {
        if(!(object instanceof ITaggable))
            throw new IllegalArgumentException(object.toString() + " is not taggable");
        return (ITaggable<T>)object;
    }

    /**
     * Remove a tag from all objects. Useful if you are trying to clear out uses of the tag to add it again.
     * @param tag
     */
    static void removeFromAll(Tag tag) {
        TagDelegate.removeFromAll(tag);
    }

}
