package edu.stanford.protege.webprotege.search;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
@AutoValue

public abstract class EntityNameMatchResult implements Comparable<EntityNameMatchResult> {

    public static EntityNameMatchResult get(int start, int end, EntityNameMatchType matchType, PrefixNameMatchType prefixNameMatchType) {
        checkArgument(start > -1);
        checkArgument(end > -1);
        checkArgument(start <= end);
        return new AutoValue_EntityNameMatchResult(start, end, matchType, prefixNameMatchType);
    }

    public abstract int getStart();

    public abstract int getEnd();

    public abstract EntityNameMatchType getMatchType();

    public abstract PrefixNameMatchType getPrefixNameMatchType();

    @Override
    public int compareTo(@Nonnull EntityNameMatchResult entityNameMatchResult) {
        final int typeDiff = this.getMatchType().compareTo(entityNameMatchResult.getMatchType());
        if(typeDiff != 0) {
            return typeDiff;
        }
        final int prefixNameMatchTypeDiff = this.getPrefixNameMatchType().compareTo(entityNameMatchResult.getPrefixNameMatchType());
        if(prefixNameMatchTypeDiff != 0) {
            return prefixNameMatchTypeDiff;
        }
        final int startDiff = this.getStart() - entityNameMatchResult.getStart();
        if(startDiff != 0) {
            return startDiff;
        }
        return this.getEnd() - entityNameMatchResult.getEnd();
    }
}
