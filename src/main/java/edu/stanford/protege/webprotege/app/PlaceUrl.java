package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.perspective.EntityTypePerspectiveMapper;
import edu.stanford.protege.webprotege.place.ItemSelection;
import edu.stanford.protege.webprotege.place.ProjectViewPlace;
import edu.stanford.protege.webprotege.place.ProjectViewPlaceTokenizer;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class PlaceUrl {

    private final ApplicationSchemeSupplier schemeSupplier;

    private final ApplicationHostSupplier hostSupplier;

    private final ApplicationPortSupplier portSupplier;

    private final ApplicationPathSupplier pathSupplier;

    private final ApplicationNameSupplier nameSupplier;

    private final EntityTypePerspectiveMapper mapper;

    /**
     * Construct a {@link PlaceUrl} object that provides URLs for places for a given application host,
     * path, port and name.
     * @param schemeSupplier The scheme for the application.
     * @param hostSupplier A provider for the application host.
     * @param portSupplier A provider for the application port.
     * @param pathSupplier A provider for the application path.
     * @param nameSupplier A provider for the application name.
     * @param mapper An {@link EntityTypePerspectiveMapper} that will be used to retrieve the perspective id that
     *               should be shown for a given entity type.  For example for OWLClass entities the "Classes"
     *               perspective might be shown.
     */
    @Inject
    public PlaceUrl(@Nonnull ApplicationSchemeSupplier schemeSupplier,
                    @Nonnull ApplicationHostSupplier hostSupplier,
                    @Nonnull ApplicationPortSupplier portSupplier,
                    @Nonnull ApplicationPathSupplier pathSupplier,
                    @Nonnull ApplicationNameSupplier nameSupplier,
                    @Nonnull EntityTypePerspectiveMapper mapper) {
        this.schemeSupplier = checkNotNull(schemeSupplier);
        this.hostSupplier = checkNotNull(hostSupplier);
        this.portSupplier = checkNotNull(portSupplier);
        this.pathSupplier = checkNotNull(pathSupplier);
        this.nameSupplier = checkNotNull(nameSupplier);
        this.mapper = checkNotNull(mapper);
    }

    /**
     * Gets the application Url.  This is the root Url of the WebProtege application.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationUrl() {
        return createUrl(null);
    }

    /**
     * Gets the application anchor.  This is the name of the WebProtege application surrounded by
     * anchor tags whose href is the value provided by {@link #getApplicationUrl()}.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationAnchor() {
        return String.format("<a href=\"%s\">%s</a>", nameSupplier.get(), getApplicationUrl());
    }

    /**
     * Get the Url for the specified project.
     * @param projectId The project whose Url will be returned.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getProjectUrl(@Nonnull ProjectId projectId) {
        ProjectViewPlace place = new ProjectViewPlace(projectId, mapper.getDefaultPerspectiveId(), ItemSelection.empty());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);
    }

    /**
     * Gets the Url for the specified entity in the specified project.
     * @param projectId The project.
     * @param entity The entity.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getEntityUrl(@Nonnull ProjectId projectId,
                               @Nonnull OWLEntity entity) {
        ProjectViewPlace place = new ProjectViewPlace(projectId,
                                                      mapper.getPerspectiveId(entity.getEntityType()),
                                                      ItemSelection.builder().addEntity(entity).build());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);

    }

    private String getProjectPlaceFragment(ProjectViewPlace place) {
        ProjectViewPlaceTokenizer tokenizer = new ProjectViewPlaceTokenizer();
        return tokenizer.getToken(place);
    }


    private String createUrl(@Nullable String fragment) {
        try {
            final URI uri;
            String scheme = this.schemeSupplier.get().name().toLowerCase();
            Optional<Integer> thePort = portSupplier.get();
            if(thePort.isPresent()) {
                uri = new URI(scheme,
                              null,
                              hostSupplier.get(),
                              thePort.get(),
                              pathSupplier.get(),
                              null,
                              fragment);
            }
            else {
                uri = new URI(scheme,
                              hostSupplier.get(),
                              pathSupplier.get(),
                              fragment);
            }
            return uri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
