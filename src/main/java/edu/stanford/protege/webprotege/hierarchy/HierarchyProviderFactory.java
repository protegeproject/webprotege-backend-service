package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

public class HierarchyProviderFactory {

    private final ClassHierarchyProviderFactory classHierarchyProviderFactory;

    private final AnnotationPropertyHierarchyProviderFactory annotationPropertyHierarchyProviderFactory;

    private final ObjectPropertyHierarchyProviderFactory objectPropertyHierarchyProviderFactory;

    private final DataPropertyHierarchyProviderFactory dataPropertyHierarchyProviderFactory;

    public HierarchyProviderFactory(ClassHierarchyProviderFactory classHierarchyProviderFactory,
                                    AnnotationPropertyHierarchyProviderFactory annotationPropertyHierarchyProviderFactory,
                                    ObjectPropertyHierarchyProviderFactory objectPropertyHierarchyProviderFactory,
                                    DataPropertyHierarchyProviderFactory dataPropertyHierarchyProviderFactory) {
        this.classHierarchyProviderFactory = classHierarchyProviderFactory;
        this.annotationPropertyHierarchyProviderFactory = annotationPropertyHierarchyProviderFactory;
        this.objectPropertyHierarchyProviderFactory = objectPropertyHierarchyProviderFactory;
        this.dataPropertyHierarchyProviderFactory = dataPropertyHierarchyProviderFactory;
    }

    @Nonnull
    public Optional<HierarchyProvider<OWLEntity>> createHierarchyProvider(HierarchyDescriptor descriptor) {
        if (descriptor instanceof ClassHierarchyDescriptor c) {
            return Optional.of(new HierarchyProviderGuard<>(OWLClass.class, classHierarchyProviderFactory.getClassHierarchyProvider(c.roots())));
        }
        else if (descriptor instanceof ObjectPropertyHierarchyDescriptor d) {
            return Optional.of(new HierarchyProviderGuard<>(OWLObjectProperty.class, objectPropertyHierarchyProviderFactory.getObjectPropertyHierarchyProvider(d.roots())));
        }
        else if (descriptor instanceof DataPropertyHierarchyDescriptor d) {
            return Optional.of(new HierarchyProviderGuard<>(OWLDataProperty.class, dataPropertyHierarchyProviderFactory.getDataPropertyHierarchyProvider(d.roots())));
        }
        else if (descriptor instanceof AnnotationPropertyHierarchyDescriptor d) {
            return Optional.of(new HierarchyProviderGuard<>(OWLAnnotationProperty.class, annotationPropertyHierarchyProviderFactory.getAnnotationPropertyHierarchyProvider(d.roots())));
        }
        else {
            return Optional.empty();
        }
    }
}
