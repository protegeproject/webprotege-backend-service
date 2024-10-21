package edu.stanford.protege.webprotege.hierarchy;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HierarchyIdTest {

    @Test
    public void shouldReturnClassHierarchyIsBuiltIn() {
        assertThat(HierarchyId.CLASS_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnObjectPropertyHierarchyIsBuiltIn() {
        assertThat(HierarchyId.OBJECT_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnDataPropertyHierarchyIsBuiltIn() {
        assertThat(HierarchyId.DATA_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnAnnotationPropertyHierarchyIsBuiltIn() {
        assertThat(HierarchyId.ANNOTATION_PROPERTY_HIERARCHY.isBuiltIn()).isTrue();
    }

    @Test
    public void shouldReturnOtherHierarchyIsNotBuiltIn() {
        assertThat(HierarchyId.get("other").isBuiltIn()).isFalse();
    }
}