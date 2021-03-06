package edu.stanford.protege.webprotege.index;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.individuals.InstanceRetrievalMode;
import edu.stanford.protege.webprotege.common.Page;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
@AutoValue

public abstract class IndividualsQueryResult {

    public static IndividualsQueryResult get(Page<OWLNamedIndividual> queryResultPage,
                                             long individualsCount,
                                             OWLClass type,
                                             InstanceRetrievalMode mode) {
        return new AutoValue_IndividualsQueryResult(queryResultPage,
                                                    individualsCount,
                                                    type,
                                                    mode);
    }

    @Nonnull
    public abstract Page<OWLNamedIndividual> getIndividuals();

    public abstract long getIndividualsCount();

    @Nonnull
    public abstract OWLClass getType();

    @Nonnull
    public abstract InstanceRetrievalMode getMode();
}
