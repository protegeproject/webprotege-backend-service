package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetMatchingCriteriaActionHandler extends AbstractProjectActionHandler<GetMatchingCriteriaAction, GetMatchingCriteriaResult> {

    @Nonnull
    private final MatcherFactory matcherFactory;


    public GetMatchingCriteriaActionHandler(@NotNull AccessManager accessManager, @Nonnull MatcherFactory matcherFactory) {
        super(accessManager);
        this.matcherFactory = matcherFactory;
    }

    @NotNull
    @Override
    public Class<GetMatchingCriteriaAction> getActionClass() {
        return GetMatchingCriteriaAction.class;
    }

    @NotNull
    @Override
    public GetMatchingCriteriaResult execute(@NotNull GetMatchingCriteriaAction action, @NotNull ExecutionContext executionContext) {

        Set<String> response = new HashSet<>();
        OWLEntity owlEntity = new OWLClassImpl(action.entitiyIri());

        for(String key : action.criteriaMap().keySet()) {
            for(CompositeRootCriteria criteria: action.criteriaMap().get(key)) {
                Matcher<OWLEntity> matcher = matcherFactory.getMatcher(criteria);
                if(matcher.matches(owlEntity)) {
                    response.add(key);
                }
            }

        }

        return new GetMatchingCriteriaResult(new ArrayList<>(response));
    }
}