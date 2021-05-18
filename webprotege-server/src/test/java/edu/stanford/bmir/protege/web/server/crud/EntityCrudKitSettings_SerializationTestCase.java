package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.criteria.HierarchyFilterType;
import edu.stanford.bmir.protege.web.server.match.criteria.SubClassOfCriteria;
import edu.stanford.bmir.protege.web.server.DataFactory;
import edu.stanford.bmir.protege.web.server.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.bmir.protege.web.server.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.server.crud.gen.IncrementingPatternDescriptor;
import edu.stanford.bmir.protege.web.server.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class EntityCrudKitSettings_SerializationTestCase {

    private EntityCrudKitSettings<UuidSuffixSettings> settings;

    @Before
    public void setUp() {
        settings = EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(),
                                             UuidSuffixSettings.get(),
                                             GeneratedAnnotationsSettings.get(ImmutableList.of(
                                                     GeneratedAnnotationDescriptor.get(DataFactory.get().getRDFSLabel(),
                                                                                       IncrementingPatternDescriptor.get(
                                                                                               100_000,
                                                                                               "%d"),
                                                                                       SubClassOfCriteria.get(DataFactory.getOWLThing(),
                                                                                                              HierarchyFilterType.DIRECT)))));
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(settings, EntityCrudKitSettings.class);
    }
}
