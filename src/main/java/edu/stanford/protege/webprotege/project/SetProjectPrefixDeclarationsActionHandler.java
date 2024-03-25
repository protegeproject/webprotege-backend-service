package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_PROJECT_PREFIXES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2018
 */
public class SetProjectPrefixDeclarationsActionHandler extends AbstractProjectActionHandler<SetProjectPrefixDeclarationsAction, SetProjectPrefixDeclarationsResult> {

    @Nonnull
    private final PrefixDeclarationsStore store;

    @Inject
    public SetProjectPrefixDeclarationsActionHandler(@Nonnull AccessManager accessManager,
                                                     @Nonnull PrefixDeclarationsStore store) {
        super(accessManager);
        this.store = checkNotNull(store);
    }

    @Nonnull
    @Override
    public Class<SetProjectPrefixDeclarationsAction> getActionClass() {
        return SetProjectPrefixDeclarationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectPrefixDeclarationsAction action) {
        return EDIT_PROJECT_PREFIXES;
    }

    @Nonnull
    @Override
    public SetProjectPrefixDeclarationsResult execute(@Nonnull SetProjectPrefixDeclarationsAction action, @Nonnull ExecutionContext executionContext) {
        Map<String, String> decls = action.prefixDeclarations().stream()
                                          .collect(Collectors.toMap(PrefixDeclaration::getPrefixName,
                                                                    PrefixDeclaration::getPrefix));
        PrefixDeclarations prefixDeclarations = PrefixDeclarations.get(action.projectId(), decls);
        store.save(prefixDeclarations);
        return new SetProjectPrefixDeclarationsResult(action.projectId(), action.prefixDeclarations());
    }
}
