package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-04
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FreshEntityIri_TestCase {


    public static final String DISCRIMINATOR = "12345678-1234-1234-1234-123456789abc";

    public static final String LANG_TAG = "lang-tag";

    public static final String SUPPLIED_NAME = "Supplied Name";

    public static final IRI parentIriB = IRI.create("http://example.orf/ParentIriB");

    private final IRI parentIriA = IRI.create("http://example.org/ParentIriA");

    private final ImmutableSet<IRI> parentIris = ImmutableSet.of(parentIriA,
                                                                 parentIriB);

    private FreshEntityIri freshEntityIri;

    @Mock
    private OWLClass clsA;

    @Mock
    private OWLClass clsB;

    @Mock
    private OWLEntityProvider entityProvider;

    @BeforeEach
    public void setUp() {
        freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME,
                                            LANG_TAG,
                                            DISCRIMINATOR,
                                            parentIris);

        entityProvider = mock(OWLEntityProvider.class);
        when(entityProvider.getOWLClass(parentIriA))
                .thenReturn(clsA);
        when(entityProvider.getOWLClass(parentIriB))
                .thenReturn(clsB);
    }
    

    @Test
    public void shouldGetFreshEntityIriWithGivenSuppliedName() {
        assertThat(freshEntityIri.getSuppliedName(), is(equalTo(SUPPLIED_NAME)));
    }

    @Test
    public void shouldGetFreshEntityIriWitGivenLangTag() {
        assertThat(freshEntityIri.getLangTag(), is(equalTo(LANG_TAG)));
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenDiscriminator() {
        assertThat(freshEntityIri.getDiscriminator(), is(equalTo(DISCRIMINATOR)));
    }

    @Test
    public void shouldGetFreshEntityIriWithGivenParentIris() {
        assertThat(freshEntityIri.getParentIris(), is(equalTo(parentIris)));
    }
    
    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNPE_If_SuppliedName_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        FreshEntityIri.get(null, LANG_TAG, DISCRIMINATOR, parentIris);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNPE_If_LangTag_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        FreshEntityIri.get(SUPPLIED_NAME, null, DISCRIMINATOR, parentIris);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNPE_If_Discriminator_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, null, parentIris);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNPE_If_ParentIris_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, DISCRIMINATOR, null);
     });
}

    @Test
    public void shouldAcceptEmptySuppliedName() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get("", LANG_TAG, DISCRIMINATOR, parentIris);
        assertThat(freshEntityIri.getSuppliedName(), is(""));
    }

    @Test
    public void shouldAcceptEmptyLangTag() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, "", DISCRIMINATOR, parentIris);
        assertThat(freshEntityIri.getLangTag(), is(""));
    }

    @Test
    public void shouldAcceptEmptyDiscriminator() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, "", parentIris);
        assertThat(freshEntityIri.getDiscriminator(), is(""));
    }

    @Test
    public void shouldAcceptEmptyParentIris() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, DISCRIMINATOR, ImmutableSet.of());
        assertThat(freshEntityIri.getParentIris(), is(ImmutableSet.of()));
    }

    @Test
    public void shouldRoundTrip() {
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptySuppliedName() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get("", LANG_TAG, DISCRIMINATOR, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyLangTag() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, "", DISCRIMINATOR, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyDiscriminator() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, "", parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmptyParentIris() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG, DISCRIMINATOR, ImmutableSet.of());
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripEmpty() {
        FreshEntityIri freshEntityIri = FreshEntityIri.get("", "", "", ImmutableSet.of());
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripSuppliedNameWithHash() {
        String suppliedName = "The#Supplied#Name";
        FreshEntityIri freshEntityIri = FreshEntityIri.get(suppliedName, LANG_TAG, DISCRIMINATOR, parentIris);
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripParentIriWithAmpersand() {
        IRI parentIri = IRI.create("http://example.org/A&B");
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG,
                                                           DISCRIMINATOR, ImmutableSet.of(parentIri));
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldRoundTripParentIriWithHash() {
        IRI parentIri = IRI.create("http://example.org#A");
        FreshEntityIri freshEntityIri = FreshEntityIri.get(SUPPLIED_NAME, LANG_TAG,
                                                           DISCRIMINATOR, ImmutableSet.of(parentIri));
        assertRoundTrips(freshEntityIri);
    }

    @Test
    public void shouldReturnTrueForFreshEntityIri() {
        IRI iri = freshEntityIri.getIri();
        assertThat(FreshEntityIri.isFreshEntityIri(iri), is(true));
    }

    @Test
    public void shouldReturnFalseForNonFreshEntityIri() {
        assertThat(FreshEntityIri.isFreshEntityIri(IRI.create("http://example.org/A")), is(false));
    }

    public void assertRoundTrips(@Nonnull FreshEntityIri freshEntityIri) {
        String iriString = freshEntityIri.getIri().toString();
        FreshEntityIri parsedFreshEntityIri = FreshEntityIri.parse(iriString);
        assertThat(parsedFreshEntityIri, is(equalTo(freshEntityIri)));
    }

    @Test
    public void shouldGetFreshEntityForOwlClass() {
        ImmutableList<OWLEntity> parentEntities = freshEntityIri.getParentEntities(entityProvider, EntityType.CLASS);
        assertThat(parentEntities, containsInAnyOrder(clsA, clsB));
    }

    @Test
    public void shouldGetFreshEntityForOwlNamedIndividual() {
        ImmutableList<OWLEntity> parentEntities = freshEntityIri.getParentEntities(entityProvider, EntityType.NAMED_INDIVIDUAL);
        assertThat(parentEntities, containsInAnyOrder(clsA, clsB));
    }
}
