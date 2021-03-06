package edu.stanford.protege.webprotege.change;



import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.EntityRenamer;
import edu.stanford.protege.webprotege.index.ProjectSignatureIndex;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/09/2013
 */
public class FindAndReplaceIRIPrefixChangeGenerator implements ChangeListGenerator<Collection<OWLEntity>> {

    @Nonnull
    private final String fromPrefix;

    @Nonnull
    private final String toPrefix;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final EntityRenamer entityRenamer;

    @Nonnull
    private final ChangeRequestId changeRequestId;


    public FindAndReplaceIRIPrefixChangeGenerator(@Nonnull ChangeRequestId changeRequestId,
                                                  @Nonnull String fromPrefix,
                                                  @Nonnull String toPrefix,
                                                  @Nonnull ProjectSignatureIndex projectSignatureIndex,
                                                  @Nonnull EntityRenamer entityRenamer) {
        this.changeRequestId = changeRequestId;
        this.fromPrefix = checkNotNull(fromPrefix);
        this.toPrefix = checkNotNull(toPrefix);
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
        this.entityRenamer = checkNotNull(entityRenamer);
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<Collection<OWLEntity>> generateChanges(ChangeGenerationContext context) {
        var builder = OntologyChangeList.<Collection<OWLEntity>>builder();
        var renameMap = new HashMap<OWLEntity, IRI>();
        projectSignatureIndex.getSignature()
                             .filter(entity -> !entity.isBuiltIn())
                             .filter(this::beginsWithFromPrefix)
                             .forEach(entity -> {
                                 var iri = entity.getIRI();
                                 var toIri = IRI.create(toPrefix + iri.subSequence(fromPrefix.length(), iri.length()));
                                 renameMap.put(entity, toIri);
                             });
        var changeList = entityRenamer.generateChanges(renameMap);
        builder.addAll(changeList);
        return builder.build(renameMap.keySet());
    }

    private boolean beginsWithFromPrefix(OWLEntity entity) {
        return entity.getIRI().toString().startsWith(fromPrefix);
    }

    @Override
    public Collection<OWLEntity> getRenamedResult(Collection<OWLEntity> result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Collection<OWLEntity>> result) {
        return String.format("Replaced IRI prefix <%s> with <%s>", fromPrefix, toPrefix);
    }
}
