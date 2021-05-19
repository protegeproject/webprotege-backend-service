package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.util.UUIDUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class DefaultOntologyIdManagerImpl_TestCase {

    private DefaultOntologyIdManagerImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Before
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
