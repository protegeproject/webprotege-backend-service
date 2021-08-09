package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.protege.webprotege.match.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EntitySearchFilterRepositoryImpl_IT {

    @Autowired
    private EntitySearchFilterRepositoryImpl repository;

    private ProjectId projectId;

    @Before
    public void setUp() throws Exception {
        projectId = ProjectId.get(UUID.randomUUID().toString());
    }

    @Test
    public void shouldCreateIndexesWithoutError() {
        repository.ensureIndexes();
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveEntitySearchFilter() {
        EntitySearchFilter filter = saveFirstSearchFilter();
        var savedFilters = repository.getSearchFilters(projectId);
        assertThat(savedFilters, contains(filter));
    }

    @Test
    public void shouldSaveDuplicates() {
        EntitySearchFilter filter = saveFirstSearchFilter();
        repository.saveSearchFilters(ImmutableList.of(filter));
        var filters = repository.getSearchFilters(projectId);
        assertThat(filters, contains(filter));
    }

    @Test
    public void shouldUpdateFilters() {
        var filter = saveFirstSearchFilter();
        var updatedFilter = EntitySearchFilter.get(filter.getId(),
                                                   filter.getProjectId(),
                                                   LanguageMap.of("en", "MyOtherFilter"),
                                                   EntityIsDeprecatedCriteria.get());
        repository.saveSearchFilters(ImmutableList.of(filter));
        repository.saveSearchFilters(ImmutableList.of(updatedFilter));
        var filters = repository.getSearchFilters(projectId);
        assertThat(filters, contains(updatedFilter));
    }

    @After
    public void tearDown() throws Exception {

    }

    private EntitySearchFilter saveFirstSearchFilter() {
        var filter = EntitySearchFilter.get(EntitySearchFilterId.createFilterId(),
                                            projectId,
                                            LanguageMap.of("en", "MyFilter"),
                                            EntityIsNotDeprecatedCriteria.get());
        repository.saveSearchFilters(ImmutableList.of(filter));
        return filter;
    }


}