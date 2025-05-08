package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HierarchyDescriptorRuleSelectorTest {

    @Mock
    private HierarchyDescriptorRulesRepository repository;

    @Mock
    private HierarchyDescriptorRuleDisplayContextMatcher matcher;

    private HierarchyDescriptorRuleSelector selector;

    private ProjectId projectId;

    private DisplayContext displayContext;

    private Set<Capability> actions;

    private HierarchyDescriptorRule dummyRule;

    private ProjectHierarchyDescriptorRules projectRules;

    @BeforeEach
    void setUp() {
        selector = new HierarchyDescriptorRuleSelector(repository, matcher);
        projectId = ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174999");
        displayContext = mock(DisplayContext.class);
        actions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));
        dummyRule = HierarchyDescriptorRule.create(ClassHierarchyDescriptor.create());
        projectRules = new ProjectHierarchyDescriptorRules(projectId, List.of(dummyRule));
    }

    @Test
    void testSelectRuleMatches() {
        when(repository.find(projectId)).thenReturn(Optional.of(projectRules));
        when(matcher.matches(dummyRule, displayContext, actions)).thenReturn(true);

        var result = selector.selectRule(projectId, displayContext, actions);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(dummyRule);
    }

    @Test
    void testSelectRuleNoMatch() {
        when(repository.find(projectId)).thenReturn(Optional.of(projectRules));
        when(matcher.matches(dummyRule, displayContext, actions)).thenReturn(false);

        var result = selector.selectRule(projectId, displayContext, actions);
        assertThat(result).isNotPresent();
    }

    @Test
    void testSelectRuleEmptyRepository() {
        when(repository.find(projectId)).thenReturn(Optional.empty());

        var result = selector.selectRule(projectId, displayContext, actions);

        assertThat(result).isNotPresent();
    }
}
