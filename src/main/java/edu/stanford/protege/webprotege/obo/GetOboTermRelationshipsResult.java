package edu.stanford.protege.webprotege.obo;


import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermRelationshipsResult implements Result {

    private OBOTermRelationships relationships;

    private GetOboTermRelationshipsResult(@Nonnull OBOTermRelationships relationships) {
        this.relationships = checkNotNull(relationships);
    }


    private GetOboTermRelationshipsResult() {
    }

    public static GetOboTermRelationshipsResult create(@Nonnull OBOTermRelationships relationships) {
        return new GetOboTermRelationshipsResult(relationships);
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }

    @Override
    public int hashCode() {
        return relationships.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermRelationshipsResult)) {
            return false;
        }
        GetOboTermRelationshipsResult other = (GetOboTermRelationshipsResult) obj;
        return this.relationships.equals(other.relationships);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermRelationshipsResult")
                .addValue(relationships)
                .toString();
    }
}
