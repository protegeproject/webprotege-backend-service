package edu.stanford.protege.webprotege.individuals;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsAction implements ProjectAction<GetIndividualsResult> {

    private ProjectId projectId;

    @Nullable
    private OWLClass type;

    @Nullable
    private PageRequest pageRequest;

    private String searchString;

    private InstanceRetrievalMode instanceRetrievalMode;



    private GetIndividualsAction() {
    }

    /**
     * Specifies that the individuals of the specified type should be retrieved from the specified project.  The range
     * argument allows optional pagination.
     *
     * @param projectId The projectId.  Not {@code null}.
     * @param type      The asserted type of the individuals.  Not {@code null}.  A type of owl:Thing means all individuals
     *                  in the ontology should be in the result.
     * @param filterString A string that can be used to filter results.  Can be empty to indicate
     *                    no filtering (match everything).  Individuals with browser text containing
     *                    the specified filter string will be included in the result.
     * @param pageRequest     The optional pageRequest for pagination.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    private GetIndividualsAction(@Nonnull ProjectId projectId,
                                 @Nonnull Optional<OWLClass> type,
                                 @Nonnull String filterString,
                                 @Nonnull InstanceRetrievalMode instanceRetrievalMode,
                                 @Nonnull Optional<PageRequest> pageRequest) {
        this.projectId = checkNotNull(projectId);
        this.type = checkNotNull(type).orElse(null);
        this.searchString = checkNotNull(filterString);
        this.instanceRetrievalMode = checkNotNull(instanceRetrievalMode);
        this.pageRequest = pageRequest.orElse(null);
    }

    public static GetIndividualsAction create(@Nonnull ProjectId projectId,
                                              @Nonnull Optional<OWLClass> type,
                                              @Nonnull String filterString,
                                              @Nonnull InstanceRetrievalMode instanceRetrievalMode,
                                              @Nonnull Optional<PageRequest> pageRequest) {
        return new GetIndividualsAction(projectId, type, filterString, instanceRetrievalMode, pageRequest);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the type of the requested individuals.
     *
     * @return The requested type.  This could be owl:Thing.  Not {@code null}.
     */
    @Nonnull
    public Optional<OWLClass> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Gets the search string.
     * @return The search string.  An empty string matches all results.
     */
    @Nonnull
    public String getSearchString() {
        return searchString;
    }

    public InstanceRetrievalMode getInstanceRetrievalMode() {
        return instanceRetrievalMode;
    }

    /**
     * Gets the page request
     *
     * @return Gets the page request.
     */
    public Optional<PageRequest> getPageRequest() {
        return Optional.ofNullable(pageRequest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetIndividualsAction)) {
            return false;
        }
        GetIndividualsAction that = (GetIndividualsAction) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(type, that.type) && Objects.equals(
                pageRequest,
                that.pageRequest) && Objects.equals(searchString,
                                                    that.searchString) && instanceRetrievalMode == that.instanceRetrievalMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, type, pageRequest, searchString, instanceRetrievalMode);
    }

    @Override
    public String toString() {
        return "GetIndividualsAction{" + "projectId=" + projectId + ", type=" + type + ", pageRequest=" + pageRequest + ", searchString='" + searchString + '\'' + ", instanceRetrievalMode=" + instanceRetrievalMode + '}';
    }
}
