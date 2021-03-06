package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudContext {

    private final ProjectId projectId;

    private final PrefixedNameExpander prefixedNameExpander;

    private final UserId userId;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nullable
    private DictionaryLanguage dictionaryLanguage;

    @Nonnull
    private final OWLOntologyID targetOntologyId;

    public EntityCrudContext(@Nonnull ProjectId projectId,
                             @Nonnull ProjectDetailsRepository projectDetailsRepository,
                             @Nonnull UserId userId,
                             @Nonnull PrefixedNameExpander prefixedNameExpander,
                             @Nonnull OWLOntologyID targetOntologyId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.prefixedNameExpander = checkNotNull(prefixedNameExpander);
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
        this.targetOntologyId = checkNotNull(targetOntologyId);
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public PrefixedNameExpander getPrefixedNameExpander() {
        return prefixedNameExpander;
    }

    @Nonnull
    public OWLOntologyID getTargetOntologyId() {
        return targetOntologyId;
    }

    @Nonnull
    public DictionaryLanguage getDictionaryLanguage() {
        if(dictionaryLanguage == null) {
            dictionaryLanguage = projectDetailsRepository.findOne(projectId)
                                                                .map(ProjectDetails::getDefaultDictionaryLanguage)
                                                                .orElse(DictionaryLanguage.rdfsLabel(""));
        }
        return dictionaryLanguage;
    }
}
