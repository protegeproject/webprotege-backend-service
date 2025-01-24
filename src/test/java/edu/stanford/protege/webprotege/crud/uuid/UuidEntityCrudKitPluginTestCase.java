package edu.stanford.protege.webprotege.crud.uuid;

import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UuidEntityCrudKitPluginTestCase {


    private UuidEntityCrudKitPlugin plugin;

    @Mock
    private UuidSuffixKit suffixKit;

    private UuidEntityCrudKitHandlerFactory handlerFactory;

    @Mock
    private EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureIndex;

    @Mock
    private EntityIriPrefixResolver entityIriPrefixResolver;

    @BeforeEach
    public void setUp() {
        handlerFactory = new UuidEntityCrudKitHandlerFactory(new OWLDataFactoryImpl(),
                                                             entitiesInProjectSignatureIndex,
                                                             entityIriPrefixResolver);
        plugin = new UuidEntityCrudKitPlugin(suffixKit, handlerFactory);
    }

    @Test
    public void shouldGetEntityCrudKit() {
        var crudKit = plugin.getEntityCrudKit();
        assertThat(crudKit, is(suffixKit));
    }

    @Test
    public void shouldGetEntityCrudKitHandler() {
        var crudKitHandler = plugin.getEntityCrudKitHandler();
        assertThat(crudKitHandler, is(not(nullValue())));
    }

    @Test
    public void shouldGetDefaultSettings() {
        var defaultSettings = plugin.getDefaultSettings();
        assertThat(defaultSettings, is(not(nullValue())));
    }
}
