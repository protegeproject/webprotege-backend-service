package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckNonExistentIrisActionHandler
        extends AbstractProjectActionHandler<CheckNonExistentIrisAction, CheckNonExistentIrisResult> {

    private final EntitiesInProjectSignatureByIriIndex signatureIndex;

    public CheckNonExistentIrisActionHandler(
            @Nonnull AccessManager accessManager,
            @Nonnull EntitiesInProjectSignatureByIriIndex signatureIndex
    ) {
        super(accessManager);
        this.signatureIndex = signatureIndex;
    }

    @Nonnull
    @Override
    public Class<CheckNonExistentIrisAction> getActionClass() {
        return CheckNonExistentIrisAction.class;
    }

    @Nonnull
    @Override
    public CheckNonExistentIrisResult execute(
            @Nonnull CheckNonExistentIrisAction action,
            @Nonnull ExecutionContext executionContext
    ) {
        Set<IRI> nonExistentIris = action.iris().stream()
                .filter(iri -> signatureIndex.getEntitiesInSignature(iri)
                        .findFirst()
                        .isEmpty())
                .collect(Collectors.toSet());

        return CheckNonExistentIrisResult.create(nonExistentIris);
    }
}
