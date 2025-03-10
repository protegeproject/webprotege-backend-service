package edu.stanford.protege.webprotege.card;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.portlet.PortletId;
import edu.stanford.protege.webprotege.project.ProjectCache;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@WebProtegeHandler
public class GetEntityCardDescriptorsHandler implements CommandHandler<GetEntityCardDescriptorsRequest, GetEntityCardDescriptorsResponse> {

    private final ActionExecutor executor;

    public GetEntityCardDescriptorsHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityCardDescriptorsRequest.CHANNEL;
    }

    @Override
    public Class<GetEntityCardDescriptorsRequest> getRequestClass() {
        return GetEntityCardDescriptorsRequest.class;
    }

    @Override
    public Mono<GetEntityCardDescriptorsResponse> handleRequest(GetEntityCardDescriptorsRequest request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}

