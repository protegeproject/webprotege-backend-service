package edu.stanford.protege.webprotege.icd.mappers;

import edu.stanford.protege.webprotege.icd.actions.AncestorHierarchyNode;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class AncestorHierarchyNodeMapper {

    private final RenderingManager renderingManager;

    public AncestorHierarchyNodeMapper(RenderingManager renderingManager) {
        this.renderingManager = renderingManager;
    }

    /**
     * Maps an AncestorHierarchyNode of OWLEntity to an AncestorHierarchyNode of OWLEntityData.
     *
     * @param owlEntityNode the original hierarchy node containing OWLEntity as the node type.
     * @return the converted hierarchy node containing OWLEntityData as the node type.
     */
    public <T extends OWLEntity> AncestorHierarchyNode<OWLEntityData> map(AncestorHierarchyNode<T> owlEntityNode) {
        // Create the new mapped node
        AncestorHierarchyNode<OWLEntityData> mappedNode = new AncestorHierarchyNode<>();
        // Convert the OWLEntity element to OWLEntityData using the rendering manager.
        mappedNode.setNode(renderingManager.getRendering(owlEntityNode.getNode()));
        
        // Recursively map each child node.
        List<AncestorHierarchyNode<OWLEntityData>> mappedChildren = owlEntityNode.getChildren()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
        mappedNode.setChildren(mappedChildren);
        
        return mappedNode;
    }
}
