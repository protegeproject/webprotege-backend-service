package edu.stanford.protege.webprotege.obo;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermSynonymsResult implements Result {

    private ImmutableList<OBOTermSynonym> synonyms;

    private GetOboTermSynonymsResult(@Nonnull List<OBOTermSynonym> synonyms) {
        this.synonyms = ImmutableList.copyOf(synonyms);
    }


    private GetOboTermSynonymsResult() {
    }

    public static GetOboTermSynonymsResult create(@Nonnull List<OBOTermSynonym> synonyms) {
        return new GetOboTermSynonymsResult(synonyms);
    }

    public List<OBOTermSynonym> getSynonyms() {
        return synonyms;
    }

    @Override
    public int hashCode() {
        return synonyms.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermSynonymsResult)) {
            return false;
        }
        GetOboTermSynonymsResult other = (GetOboTermSynonymsResult) obj;
        return this.synonyms.equals(other.synonyms);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermSynonymsResult")
                .addValue(synonyms)
                .toString();
    }
}
