package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-28
 */
public class BuiltInPerspectiveLoader {

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public BuiltInPerspectiveLoader(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    public BuiltInPerspective load(String path) throws IOException {
        var classLoader = Thread.currentThread().getContextClassLoader();
        try(var input = classLoader.getResourceAsStream(path)) {
            if(input == null) {
                throw new RuntimeException("Builtin perspective not found: " + path);
            }
            return objectMapper.readValue(input, BuiltInPerspective.class);
        }
    }
}
