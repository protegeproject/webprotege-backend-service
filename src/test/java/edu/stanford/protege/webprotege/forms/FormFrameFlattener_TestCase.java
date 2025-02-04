package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FormFrameFlattener_TestCase {

    private FormFrameFlattener formFrameFlattener;

    @Mock
    private FormEntitySubject subject, otherSubject;

    @Mock
    private OWLClass parentA, parentB, subClassA, subClassB;

    @Mock
    private OWLNamedIndividual instanceA, instanceB;

    @Mock
    private PlainPropertyValue propertyValueA, propertyValueB;

    private FormFrame parentFrame;

    @BeforeEach
    public void setUp() {
        formFrameFlattener = new FormFrameFlattener();

        var parentsInA = ImmutableSet.of(parentA);
        var subClassesInA = ImmutableSet.of(subClassA);
        var instancesInA = ImmutableSet.of(instanceA);
        var propertyValuesInA = ImmutableSet.of(propertyValueA);

        var parentsInB = ImmutableSet.of(parentB);
        var subClassesInB = ImmutableSet.of(subClassB);
        var instancesInB = ImmutableSet.of(instanceB);
        var propertyValuesInB = ImmutableSet.of(propertyValueB);

        var frameA = FormFrame.get(subject,
                                   parentsInA,
                                   subClassesInA,
                                   instancesInA,
                                   propertyValuesInA,
                                   ImmutableSet.of());
        var frameB = FormFrame.get(subject,
                                   parentsInB,
                                   subClassesInB,
                                   instancesInB,
                                   propertyValuesInB,
                                   ImmutableSet.of());
        parentFrame = FormFrame.get(otherSubject,
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(),
                                        ImmutableSet.of(frameA, frameB));


    }

    @Test
    public void shouldFlattenFrames() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        assertThat(flattenedFrames.size(), is(2));
    }

    @Test
    public void shouldNotContainNesting() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        var frameWithOtherSubject = flattenedFrames.stream().filter(f -> f.getSubject().equals(otherSubject)).findFirst().orElseThrow();
        assertThat(frameWithOtherSubject.getSubject(), is(otherSubject));
        assertThat(frameWithOtherSubject.getNestedFrames(), hasSize(0));
    }

    @Test
    public void shouldMergeFramesWithSameSubject() {
        var flattenedFrames = formFrameFlattener.flattenAndMerge(parentFrame);
        var frameWithSubject = flattenedFrames.stream().filter(f -> f.getSubject().equals(subject)).findFirst().orElseThrow();
        assertThat(frameWithSubject.getSubject(), is(subject));
        assertThat(frameWithSubject.getClasses(), containsInAnyOrder(parentA, parentB));
        assertThat(frameWithSubject.getSubClasses(), containsInAnyOrder(subClassA, subClassB));
        assertThat(frameWithSubject.getInstances(), containsInAnyOrder(instanceA, instanceB));
        assertThat(frameWithSubject.getPropertyValues(), containsInAnyOrder(propertyValueA, propertyValueB));
    }
}
