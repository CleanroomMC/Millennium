package com.cleanroommc.millennium.common.tag;

import com.google.common.collect.HashMultimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A default implementation of ITaggable to help registry entries that don't want to provide it for themselves.
 */
public class TagDelegate<T> implements ITaggable<T> {
    private static final HashMap<ITaggable<?>, com.cleanroommc.millennium.common.tag.TagDelegate<?>> DELEGATES = new HashMap<>();
    private static final HashMultimap<Tag, com.cleanroommc.millennium.common.tag.TagDelegate<?>> DELEGATES_HOLDING_TAG = HashMultimap.create();
    private final Class<T> delegateType;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Do not use this TagDelegate instance for anything in your mod, use ITaggable directly.
     */
    @SuppressWarnings("unchecked")
    public static <T> com.cleanroommc.millennium.common.tag.TagDelegate<T> getDelegate(ITaggable<T> taggable) {
        return (com.cleanroommc.millennium.common.tag.TagDelegate<T>) DELEGATES.computeIfAbsent(taggable, t -> new com.cleanroommc.millennium.common.tag.TagDelegate<>(t.getTagDelegateType()));
    }

    /**
     * Remove a tag from every object that has it.
     * @param tag The tag to remove
     */
    public static void removeFromAll(Tag tag) {
        /* Create a new Set since delegates will be removed in the first place */
        Set<com.cleanroommc.millennium.common.tag.TagDelegate<?>> delegates = new HashSet<>(DELEGATES_HOLDING_TAG.get(tag));
        delegates.forEach(delegate -> delegate.removeTag(tag));
    }

    TagDelegate(Class<T> delegateType) {
        this.delegateType = delegateType;
    }

    @Override
    public void addTag(Tag tag) {
        tags.add(tag);
        DELEGATES_HOLDING_TAG.put(tag, this);
    }

    @Override
    public void removeTag(Tag tag) {
        tags.remove(tag);
        DELEGATES_HOLDING_TAG.remove(tag, this);
    }

    @Override
    public boolean isTag(Tag tag) {
        return tags.contains(tag);
    }

    @Override
    public Stream<Tag> getTags() {
        return tags.stream();
    }

    /**
     * DO NOT USE this unless you have a good reason to, it exists to help internal optimization only.
     */
    public Set<Tag> getTagSet() {
        return tags;
    }

    @Override
    public Class<T> getTagDelegateType() {
        return delegateType;
    }
}
