package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.protege.webprotege.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@SpringBootTest
@ExtendWith({MongoTestExtension.class, RabbitTestExtension.class,})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EntitySearchFilterRepositoryImpl_IT {

    @Autowired
    private EntitySearchFilterRepositoryImpl repository;

    private ProjectId projectId;

    @BeforeEach
    public void setUp() throws Exception {
        projectId = ProjectId.valueOf(UUID.randomUUID().toString());
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

    @AfterEach
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