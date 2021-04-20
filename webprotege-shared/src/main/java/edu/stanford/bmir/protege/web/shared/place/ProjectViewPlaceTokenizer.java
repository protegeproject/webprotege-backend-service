package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.collect.Lists;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.Namespaces;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class ProjectViewPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectViewPlace> {

    private static final String PROJECTS = "projects/";

    private static final String PERSPECTIVES = "/perspectives/";

    private static final String SELECTION = "?selection=";

    private static Pattern pattern = Pattern.compile(PROJECTS + "(.{36})" + PERSPECTIVES + "([^\\?]*)(\\" + SELECTION + "(.*))?" );

    @Override
    public boolean matches(String token) {
        return pattern.matcher(token).matches();
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ProjectViewPlace;
    }

    public ProjectViewPlace getPlace(String token) {
        token = URL.decode(token);

        Matcher matcher = pattern.matcher(token);
        if (!matcher.matches()) {
            return null;
        }
        String projectId = matcher.group(1);
        String perspectiveId = matcher.group(2);
        String selectionString = matcher.group(4);

        ProjectViewPlace.Builder builder = new ProjectViewPlace.Builder(ProjectId.get(projectId), PerspectiveId.get(perspectiveId));

        if(selectionString != null) {
            ItemTokenizer tokenizer = new ItemTokenizer();
            List<ItemToken> tokenList = tokenizer.parseTokens(selectionString);
            for(ItemToken t : tokenList) {
                OWLDataFactoryImpl dataFactory = new OWLDataFactoryImpl();
                ItemTokenParser parser = new ItemTokenParser();
                DefaultPrefixManager prefixManager = new DefaultPrefixManager();
                prefixManager.setPrefix("owl:", Namespaces.OWL.getPrefixIRI());
                List<Item<?>> entity = parser.parse(t, new DefaultItemTypeMapper(dataFactory, prefixManager));
                for(Item<?> item : entity) {
                    builder.withSelectedItem(item);
                }
            }
        }
        return builder.build();
    }

    public String getToken(ProjectViewPlace place) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROJECTS);
        sb.append(place.getProjectId().getId());
        sb.append(PERSPECTIVES);
        sb.append(place.getPerspectiveId().getId());

        List<ItemToken> itemTokens = Lists.newArrayList();
        for(Item<?> item : place.getItemSelection()) {
            String typeName = item.getAssociatedType().getName();
            itemTokens.add(new ItemToken(typeName, item.getItem().toString()));
        }
        if (!itemTokens.isEmpty()) {
            String rendering = new ItemTokenizer().renderTokens(itemTokens);
            sb.append(SELECTION);
            sb.append(rendering);
        }

        return sb.toString();
    }
}
