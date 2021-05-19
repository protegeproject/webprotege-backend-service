package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.criteria.HierarchyFilterType;
import edu.stanford.protege.webprotege.match.criteria.SubClassOfCriteria;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.gen.IncrementingPatternDescriptor;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
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
