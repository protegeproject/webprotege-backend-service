package edu.stanford.protege.webprotege.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Sep 2018
 */
public class DefaultDisplayNameSettingsFactory {

    @Inject
    public DefaultDisplayNameSettingsFactory() {
    }

    @Nonnull
    public DisplayNameSettings getDefaultDisplayNameSettings(@Nonnull String langTag) {
        ImmutableList<DictionaryLanguage> primaryLanguages;
        if (langTag.isEmpty()) {
            primaryLanguages = ImmutableList.of(DictionaryLanguage.rdfsLabel(langTag),
                                                DictionaryLanguage.prefixedName(),
                                                DictionaryLanguage.localName());
        }
        else {
            primaryLanguages = ImmutableList.of(DictionaryLanguage.rdfsLabel(langTag),
                                                DictionaryLanguage.rdfsLabel(""),
                                                DictionaryLanguage.prefixedName(),
                                                DictionaryLanguage.localName());
        }
        return DisplayNameSettings.get(
                primaryLanguages,
                ImmutableList.of()
        );
    }
}
