package edu.stanford.protege.webprotege.inject.project;

import dagger.Component;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.shortform.LuceneModule;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.io.OWLObjectRenderer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@Component(modules = {UploadedProjectModule.class, LuceneModule.class})
@ProjectSingleton
public interface UploadedProjectComponent {

    OWLObjectRenderer getOwlObjectRenderer();

    ProjectDisposablesManager getProjectDisposablesManager();
}
