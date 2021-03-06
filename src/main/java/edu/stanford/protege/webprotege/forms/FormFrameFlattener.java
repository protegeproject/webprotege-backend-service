package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.FormSubject;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Collection;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormFrameFlattener {

    @Nonnull
    public ImmutableList<FormFrame> flattenAndMerge(@Nonnull FormFrame formFrame) {
        var framesBySubject = flattenNestedFrames(formFrame);
        return mergeFormFramesWithCommonSubject(framesBySubject);

    }

    @Nonnull
    private LinkedListMultimap<FormSubject, FormFrame> flattenNestedFrames(@Nonnull FormFrame formFrame) {
        var processingQueue = new ArrayDeque<FormFrame>();
        processingQueue.push(formFrame);
        var framesBySubject = LinkedListMultimap.<FormSubject, FormFrame>create();
        while(!processingQueue.isEmpty()) {
            var currentFrame = processingQueue.poll();
            processingQueue.addAll(currentFrame.getNestedFrames());
            framesBySubject.put(currentFrame.getSubject(), currentFrame);
        }
        return framesBySubject;
    }

    @Nonnull
    private ImmutableList<FormFrame> mergeFormFramesWithCommonSubject(@Nonnull LinkedListMultimap<FormSubject, FormFrame> framesBySubject) {
        return framesBySubject.asMap()
                       .values()
                       .stream()
                       .map(this::mergeFormFrames)
                       .collect(toImmutableList());
    }


    @Nonnull
    private FormFrame mergeFormFrames(@Nonnull Collection<FormFrame> formFrames) {
        if(formFrames.isEmpty()) {
            throw new RuntimeException("Form frame set is empty");
        }
        var parents = ImmutableSet.<OWLClass>builder();
        var subClasses = ImmutableSet.<OWLClass>builder();
        var instances = ImmutableSet.<OWLNamedIndividual>builder();
        var propertyValues = ImmutableSet.<PlainPropertyValue>builder();
        FormEntitySubject subject = formFrames.iterator().next().getSubject();
        for(FormFrame formFrame : formFrames) {
            parents.addAll(formFrame.getClasses());
            instances.addAll(formFrame.getInstances());
            subClasses.addAll(formFrame.getSubClasses());
            propertyValues.addAll(formFrame.getPropertyValues());
            subject = formFrame.getSubject();
        }
        return FormFrame.get(subject,
                             parents.build(),
                             subClasses.build(),
                             instances.build(),
                             propertyValues.build(),
                             ImmutableSet.of());
    }

}
