package edu.stanford.protege.webprotege.axiom;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import jakarta.inject.Inject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
public class AxiomByTypeComparator implements Comparator<OWLAxiom> {

    private final Map<AxiomType<?>, Integer> typeIndexMap = new HashMap<>();

    @Inject
    public AxiomByTypeComparator(@AxiomTypeOrdering List<AxiomType<?>> axiomTypeOrdering) {
        for(AxiomType<?> axiomType : axiomTypeOrdering) {
            typeIndexMap.put(axiomType, typeIndexMap.size());
        }
    }

    @Override
    public int compare(OWLAxiom o1, OWLAxiom o2) {
        AxiomType<?> type1 = o1.getAxiomType();
        AxiomType<?> type2 = o2.getAxiomType();
        return getTypeIndex(type1) - getTypeIndex(type2);
    }

    private int getTypeIndex(AxiomType<?> axiomType) {
        Integer typeIndex = typeIndexMap.get(axiomType);
        if(typeIndex == null) {
            return Integer.MAX_VALUE;
        }
        else {
            return typeIndex;
        }
    }
}
