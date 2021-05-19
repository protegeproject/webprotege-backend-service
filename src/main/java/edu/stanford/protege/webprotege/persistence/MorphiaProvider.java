package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.access.RoleAssignment;
import edu.stanford.protege.webprotege.api.ApiKeyIdConverter;
import edu.stanford.protege.webprotege.api.HashedApiKeyConverter;
import edu.stanford.protege.webprotege.color.ColorConverter;
import edu.stanford.protege.webprotege.form.FormIdConverter;
import edu.stanford.protege.webprotege.tag.TagIdConverter;
import edu.stanford.protege.webprotege.user.UserActivityRecord;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThread;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.Converters;
import org.mongodb.morphia.mapping.Mapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MorphiaProvider implements Provider<Morphia> {

    @Nonnull
    private final UserIdConverter userIdConverter;

    @Nonnull
    private final OWLEntityConverter entityConverter;

    @Nonnull
    private final ProjectIdConverter projectIdConverter;

    @Nonnull
    private final ThreadIdConverter threadIdConverter;

    @Nonnull
    private final CommentIdConverter commentIdConverter;

    @Nonnull
    private final FormIdConverter formIdConverter;

    @Nonnull
    private final TagIdConverter tagIdConverter;

    @Nonnull
    private ColorConverter colorConverter;


    @Inject
    public MorphiaProvider(@Nonnull UserIdConverter userIdConverter,
                           @Nonnull OWLEntityConverter entityConverter,
                           @Nonnull ProjectIdConverter projectIdConverter,
                           @Nonnull ThreadIdConverter threadIdConverter,
                           @Nonnull CommentIdConverter commentIdConverter,
                           @Nonnull FormIdConverter formIdConverter,
                           @Nonnull TagIdConverter tagIdConverter,
                           @Nonnull ColorConverter colorConverter) {
        this.userIdConverter = userIdConverter;
        this.entityConverter = entityConverter;
        this.projectIdConverter = projectIdConverter;
        this.threadIdConverter = threadIdConverter;
        this.commentIdConverter = commentIdConverter;
        this.formIdConverter = formIdConverter;
        this.tagIdConverter = tagIdConverter;
        this.colorConverter = colorConverter;
    }

    @Override
    public Morphia get() {
        Morphia morphia = new Morphia();

        Mapper mapper = morphia.getMapper();
        mapper.getOptions().setStoreEmpties(true);

        Converters converters = mapper.getConverters();
        converters.addConverter(userIdConverter);
        converters.addConverter(entityConverter);
        converters.addConverter(projectIdConverter);
        converters.addConverter(threadIdConverter);
        converters.addConverter(commentIdConverter);
        converters.addConverter(formIdConverter);
        converters.addConverter(tagIdConverter);
        converters.addConverter(colorConverter);
        converters.addConverter(new HashedApiKeyConverter());
        converters.addConverter(new ApiKeyIdConverter());
        morphia.map(EntityDiscussionThread.class);
        morphia.map(UserActivityRecord.class);
        morphia.map(RoleAssignment.class);

        return morphia;
    }
}
