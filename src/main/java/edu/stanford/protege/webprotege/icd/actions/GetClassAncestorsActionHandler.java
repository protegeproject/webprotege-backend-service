package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("webprotege.entities.GetClassAncestors")
public class GetClassAncestorsActionHandler extends AbstractProjectActionHandler<GetClassAncestorsAction, GetClassAncestorsResult> {

    private final ClassHierarchyProvider classHierarchyProvider;

    private final RenderingManager renderingManager;

    public GetClassAncestorsActionHandler(@NotNull AccessManager accessManager, ClassHierarchyProvider classHierarchyProvider, RenderingManager renderingManager) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.renderingManager = renderingManager;
    }

    @NotNull
    @Override
    public Class<GetClassAncestorsAction> getActionClass() {
        return GetClassAncestorsAction.class;
    }

    @NotNull
    @Override
    public GetClassAncestorsResult execute(@NotNull GetClassAncestorsAction action, @NotNull ExecutionContext executionContext) {
        List<AncestorHierarchyNode<OWLEntityData>> ancestors =  classHierarchyProvider.getAncestors(new OWLClassImpl(action.classIri())).stream().map(owlCLass -> {
            OWLEntityData entityData = renderingManager.getRendering(owlCLass);
            AncestorHierarchyNode<OWLEntityData> resp = new AncestorHierarchyNode<>();
            resp.setNode(entityData);
            return resp;
        }).toList();
        AncestorHierarchyNode<OWLEntityData> response = new AncestorHierarchyNode<>();
        response.setNode(renderingManager.getRendering(new OWLClassImpl(action.classIri())));
        response.setChildren(ancestors);
        return new GetClassAncestorsResult(new AncestorHierarchyNode<>());
    }
}
