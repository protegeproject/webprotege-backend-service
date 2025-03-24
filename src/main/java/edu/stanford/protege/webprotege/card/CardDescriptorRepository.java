package edu.stanford.protege.webprotege.card;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.persistence.Repository;

import java.util.List;

public interface CardDescriptorRepository extends Repository {

    void clearCardDescriptors(ProjectId projectId);

    void setCardDescriptors(ProjectId projectId, List<CardDescriptor> cardDescriptors);

    List<CardDescriptor> getCardDescriptors(ProjectId projectId);
}
