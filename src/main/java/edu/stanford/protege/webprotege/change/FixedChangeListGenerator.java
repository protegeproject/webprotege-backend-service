package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ChangeRequest;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.owlapi.RenameMap;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class FixedChangeListGenerator<R> implements ChangeListGenerator<R> {


    @Nonnull
    private final R result;

    @Nonnull
    private final List<OntologyChange> fixedChangeList;

    @Nonnull
    private final String message;

    @Nonnull
    private final ChangeRequestId changeRequestId;

    public FixedChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                    @Nonnull List<OntologyChange> fixedChangeList,
                                    @Nonnull R result,
                                    @Nonnull String message) {
        this.changeRequestId = changeRequestId;
        this.result = checkNotNull(result);
        this.fixedChangeList = ImmutableList.copyOf(fixedChangeList);
        this.message = checkNotNull(message);
    }

    public static <S> FixedChangeListGenerator<S> get(@Nonnull ChangeRequestId changeRequestId,
                                                      List<OntologyChange> changes,
                                                      @Nonnull S result,
                                                      String message) {
        return new FixedChangeListGenerator<>(changeRequestId, changes, result, message);
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<R> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<R> builder = new OntologyChangeList.Builder<>();
        builder.addAll(fixedChangeList);
        return builder.build(result);
    }

    @Override
    public R getRenamedResult(R result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<R> result) {
        return message;
    }
}
