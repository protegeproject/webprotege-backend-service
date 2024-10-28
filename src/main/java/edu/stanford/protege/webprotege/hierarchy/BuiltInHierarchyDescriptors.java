package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.LanguageMap;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BuiltInHierarchyDescriptors {

    NamedHierarchy CLASS_HIERARCHY = new NamedHierarchy(HierarchyId.CLASS_HIERARCHY,
            LanguageMap.of("en", "Class hierarchy"),
            LanguageMap.of("en", "Provides the asserted class hierarchy, rooted at owl:Thing."),
            new ClassHierarchyDescriptor(Set.of(DataFactory.getOWLThing())));

    NamedHierarchy OBJECT_PROPERTY_HIERARCHY = new NamedHierarchy(HierarchyId.OBJECT_PROPERTY_HIERARCHY,
            LanguageMap.of("en", "Object property hierarchy"),
            LanguageMap.of("en", "Provides the asserted object property hierarchy, rooted at owl:topObjectProperty."),
            new ObjectPropertyHierarchyDescriptor(Set.of(DataFactory.getOWLTopObjectProperty())));

    NamedHierarchy DATA_PROPERTY_HIERARCHY = new NamedHierarchy(HierarchyId.DATA_PROPERTY_HIERARCHY,
            LanguageMap.of("en", "Data property hierarchy"),
            LanguageMap.of("en", "Provides the asserted data property hierarchy, rooted at owl:topDataProperty."),
            new DataPropertyHierarchyDescriptor(Set.of(DataFactory.getOWLTopDataProperty())));

    NamedHierarchy ANNOTATION_PROPERTY_HIERARCHY = new NamedHierarchy(HierarchyId.ANNOTATION_PROPERTY_HIERARCHY,
            LanguageMap.of("en", "Annotation property hierarchy"),
            LanguageMap.of("en", "Provides the annotation property hierarchy."),
            new AnnotationPropertyHierarchyDescriptor(Set.of()));

    static List<NamedHierarchy> getBuiltInDescriptors() {
        return List.of(CLASS_HIERARCHY, OBJECT_PROPERTY_HIERARCHY, DATA_PROPERTY_HIERARCHY, ANNOTATION_PROPERTY_HIERARCHY);
    }
}
