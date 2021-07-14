package edu.stanford.protege.webprotege.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@JsonTypeName("GetOboTermId")
public class GetOboTermIdResult implements Result {

    public static final String TERM = "term";

    private OWLEntity entity;

    private OBOTermId termId;


    private GetOboTermIdResult() {
    }

    @JsonCreator
    private GetOboTermIdResult(@JsonProperty(TERM) @Nonnull OWLEntity entity,
                               @JsonProperty("termId") @Nonnull OBOTermId termId) {
        this.entity = checkNotNull(entity);
        this.termId = checkNotNull(termId);
    }

    public static GetOboTermIdResult create(@Nonnull OWLEntity entity, @Nonnull OBOTermId termId) {
        return new GetOboTermIdResult(entity, termId);
    }

    @JsonProperty(TERM)
    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermId getTermId() {
        return termId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, termId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermIdResult)) {
            return false;
        }
        GetOboTermIdResult other = (GetOboTermIdResult) obj;
        return this.entity.equals(other.entity)
                && this.termId.equals(other.termId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermIdResult")
                .addValue(entity)
                .addValue(termId)
                .toString();
    }
}
