package edu.stanford.protege.webprotege.frame;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesResult implements Result {

    private ImmutableList<OntologyFrame> ontologyFrames;

    private GetOntologyFramesResult() {
    }

    private GetOntologyFramesResult(ImmutableList<OntologyFrame> ontologyFrames) {
        this.ontologyFrames = checkNotNull(ontologyFrames);
    }

    public static GetOntologyFramesResult create(ImmutableList<OntologyFrame> ontologyFrames) {
        return new GetOntologyFramesResult(ontologyFrames);
    }

    public ImmutableList<OntologyFrame> getOntologyFrames() {
        return ontologyFrames;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ontologyFrames);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOntologyFramesResult)) {
            return false;
        }
        GetOntologyFramesResult other = (GetOntologyFramesResult) obj;
        return this.ontologyFrames.equals(other.ontologyFrames);
    }

    @Override
    public String toString() {
        return toStringHelper("GetOntologyFramesResult")
                .addValue(ontologyFrames)
                .toString();
    }
}
