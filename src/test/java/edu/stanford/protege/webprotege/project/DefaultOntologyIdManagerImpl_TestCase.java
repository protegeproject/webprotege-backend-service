package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.util.UUIDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-03
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DefaultOntologyIdManagerImpl_TestCase {

    private DefaultOntologyIdManagerImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @BeforeEach
    public void setUp() {
        impl = new DefaultOntologyIdManagerImpl(projectOntologiesIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(projectOntologiesIndex));
    }

    @Test
    public void shouldGetDefaultOntologyId() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(inv -> Stream.of(ontologyId));

        assertThat(impl.getDefaultOntologyId(), is(ontologyId));
    }

    @Test
    public void shouldThrowNoSuchElementExceptionOnEmptyIdList() {
        var ontologyId = impl.getDefaultOntologyId();
        var ontologyIri = ontologyId.getOntologyIRI().toJavaUtil().orElseThrow();
        assertThat(ontologyIri.toString().matches("urn:webprotege:ontology:" + UUIDUtil.UUID_PATTERN), is(true));
    }
}
