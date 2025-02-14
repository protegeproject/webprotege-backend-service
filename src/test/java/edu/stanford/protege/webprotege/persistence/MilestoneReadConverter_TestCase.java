package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.Milestone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
public class MilestoneReadConverter_TestCase {

    public static final String THE_LABEL = "MyMilestone";

    private MilestoneReadConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new MilestoneReadConverter();
    }

    @Test
    public void shouldReadMilestone() {
        Milestone milestone = converter.convert(THE_LABEL);
        assertThat(milestone.getLabel(), is(THE_LABEL));
    }
}
