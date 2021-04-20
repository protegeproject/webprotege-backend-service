package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.place.ProjectSettingsPlace;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
public class ProjectSettingsPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectSettingsPlace> {


    private static final String PROJECTS = "projects/";

    private static final String SETTINGS = "/settings";

    private static Pattern pattern = Pattern.compile("^" + PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + SETTINGS + "$");


    @Override
    public boolean matches(String token) {
        return pattern.matcher(token).matches();
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ProjectSettingsPlace;
    }

    @Override
    public ProjectSettingsPlace getPlace(String token) {
        Matcher matcher = pattern.matcher(token);
        boolean matches = matcher.matches();
        if(!matches) {
            return null;
        }
        String projectIdString = matcher.group(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return new ProjectSettingsPlace(projectId, empty());
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(ProjectSettingsPlace place) {
        return PROJECTS + place.getProjectId().getId() + SETTINGS;
    }
}
