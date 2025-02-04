package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.inject.OverridableFileFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
public class BuiltInPrefixDeclarationsLoader_TestCase {

    @Test
    public void shouldLoadBuiltInDeclarationsFromClassPath() {
        OverridableFileFactory fileFactory = new OverridableFileFactory(new File("/tmp"));
        BuiltInPrefixDeclarationsLoader manager = new BuiltInPrefixDeclarationsLoader(fileFactory);
        BuiltInPrefixDeclarations decls = manager.getBuiltInPrefixDeclarations();
        ImmutableList<PrefixDeclaration> prefixDeclarations = decls.getPrefixDeclarations();
        assertThat(prefixDeclarations.isEmpty(), Matchers.is(false));
    }
}
