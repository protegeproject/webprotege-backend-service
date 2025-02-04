package edu.stanford.protege.webprotege.entity;

import com.google.common.base.Splitter;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public class EntityShortFormsParser {

    @Inject
    public EntityShortFormsParser() {
    }

    @Nonnull
    public List<String> parseShortForms(@Nonnull String sourceText) {
        return Splitter.on("\n")
                                     .omitEmptyStrings()
                                     .trimResults()
                                     .splitToList(sourceText);
    }
}
