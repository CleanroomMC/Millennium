package com.cleanroommc.millennium.common.tag;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class TagHelpers {
    TagHelpers() {

    }
    public static <T> Stream<Tag> streamFromSubObjects(Iterable<ITaggable<T>> subobjects) {
        Set<Tag> tagSet = null;
        for(ITaggable<T> subobject : subobjects) {
            Set<Tag> stateTags = ((TagDelegate<T>)subobject).getTagSet();
            if(!stateTags.isEmpty()){
                stateTags.forEach(tag -> System.out.println(tag));
            }
            if(tagSet == null)
                tagSet = new HashSet<>(stateTags);
            else
                tagSet.retainAll(stateTags);
        }
        if(tagSet != null)
            return tagSet.stream();
        else
            return Stream.empty();
    }
}
