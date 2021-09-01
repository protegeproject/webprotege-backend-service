package edu.stanford.protege.webprotege.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.diff.DiffElement;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
@AutoValue

public abstract class ProjectChange implements  Serializable {

    @Nonnull
    public static ProjectChange get(@Nonnull RevisionNumber revisionNumber, UserId author, long timestamp, String summary, int changeCount, Page<DiffElement<String, String>> diff) {
        return new AutoValue_ProjectChange(changeCount,
                                           revisionNumber,
                                           author,
                                           summary,
                                           timestamp,
                                           diff);
    }

    public abstract int getChangeCount();

    public abstract RevisionNumber getRevisionNumber();

    @JsonProperty("userId")
    public abstract UserId getAuthor();
    
    public abstract String getSummary();

    public abstract long getTimestamp();

    public abstract Page<DiffElement<String, String>> getDiff();
}
