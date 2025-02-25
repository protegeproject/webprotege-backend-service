package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-14
 */
public class FormSubjectTranslator {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public FormSubjectTranslator(@Nonnull OWLDataFactory dataFactory) {
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Nonnull
    public Optional<FormEntitySubject> getSubjectForPrimitive(@Nonnull OWLPrimitive primitive,
                                                              @Nonnull EntityType<?> subjectType) {
        return Optional.ofNullable(toEntity(primitive, subjectType))
                .map(FormEntitySubject::get);
    }

    @Nullable
    private OWLEntity toEntity(@Nonnull OWLPrimitive primitive,
                               @Nonnull EntityType<?> subjectType) {
        if(primitive instanceof OWLEntity) {
            return (OWLEntity) primitive;
        }
        else if(primitive instanceof IRI) {
            return dataFactory.getOWLEntity(subjectType, (IRI) primitive);
        }
        else {
            return null;
        }
    }
}
