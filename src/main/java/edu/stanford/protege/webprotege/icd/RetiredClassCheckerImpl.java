package edu.stanford.protege.webprotege.icd;

import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class RetiredClassCheckerImpl implements RetiredClassChecker {

    private final OWLLiteralExtractorManager owlLiteralExtractorManager;


    @Inject
    public RetiredClassCheckerImpl(OWLLiteralExtractorManager owlLiteralExtractorManager) {
        this.owlLiteralExtractorManager = checkNotNull(owlLiteralExtractorManager);
    }

    @Override
    public boolean isRetired(@NotNull OWLEntity entity) {

        String titleValue = getIcdTitleValueForEntity(entity);

        return titleValue.contains(IcdConstants.RETIRED_TITLE_PART);
    }

    private String getIcdTitleValueForEntity(OWLEntity entity) {

        OWLLiteral titleLiteral = owlLiteralExtractorManager.getOwlLiteralValueFromEntity(entity, IcdProperties.TITLE_PROP, IcdProperties.LABEL_PROP);

        return titleLiteral.getLiteral();
    }
}
