package edu.stanford.protege.webprotege.issues.mention;


import edu.stanford.protege.webprotege.issues.Mention;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class EntityMention extends Mention {

    @Nonnull
    private OWLEntity entity;


    public EntityMention(@Nonnull OWLEntity entity) {
        this.entity = entity;
    }


    private EntityMention() {
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }


    @Override
    public String toString() {
        return toStringHelper("EntityMention")
                .addValue(entity)
                .toString();
    }
}
