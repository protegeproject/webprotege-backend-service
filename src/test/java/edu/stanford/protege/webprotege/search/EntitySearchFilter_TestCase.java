package edu.stanford.protege.webprotege.search;

import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.match.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EntitySearchFilter_TestCase {

    @Mock
    private EntitySearchFilterId id;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private LanguageMap label;

    @Mock
    private EntityMatchCriteria matchCriteria;

    private EntitySearchFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = EntitySearchFilter.get(id,
                                        projectId,
                                        label,
                                        matchCriteria);
    }

    @Test
    public void shouldReturnProvidedId() {
        assertThat(filter.getId(), is(id));
    }

    @Test
    public void shouldReturnProvidedProjectId() {
        assertThat(filter.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnProvidedLabel() {
        assertThat(filter.getLabel(), is(label));
    }

    @Test
    public void shouldReturnProvidedCriteria() {
        assertThat(filter.getEntityMatchCriteria(), is(matchCriteria));
    }
}