package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.Milestone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MilestoneWriteConverter_TestCase {

    public static final String THE_LABEL = "MyMilestone";

    private MilestoneWriteConverter converter;

    @Mock
    private Milestone milestone;

    @BeforeEach
    public void setUp() {
        converter = new MilestoneWriteConverter();
        when(milestone.getLabel()).thenReturn(THE_LABEL);
    }

    @Test
    public void shouldWriteMilestone() {
        String s = converter.convert(milestone);
        assertThat(s, is(THE_LABEL));
    }
}
