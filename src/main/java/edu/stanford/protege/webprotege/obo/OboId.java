package edu.stanford.protege.webprotege.obo;

import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
public class OboId {

    private final static Pattern PATTERN = Pattern.compile("/([A-Z|a-z]+(_[A-Z|a-z]+)?)_([0-9]+)$");

    @Nonnull
    public static Optional<String> getOboId(@Nonnull IRI iri) {
        Matcher matcher = PATTERN.matcher(iri.toString());
        if(!matcher.find()) {
            return Optional.empty();
        }
        String idSpace = matcher.group(1);
        String id = matcher.group(3);
        return Optional.of(idSpace + ":" + id);
    }
}
