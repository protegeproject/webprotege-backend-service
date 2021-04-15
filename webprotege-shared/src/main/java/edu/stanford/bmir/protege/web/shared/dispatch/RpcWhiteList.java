package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.bulkop.*;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.individuals.*;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.lang.*;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge_add.*;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import edu.stanford.bmir.protege.web.shared.tag.*;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.bmir.protege.web.shared.watches.*;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.ParentNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplPlain;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 * <p>
 * Do not use this class in any client code.  It is here to whitelist objects
 * that don't get added to the serialization whitelist.
 */
public class RpcWhiteList implements IsSerializable, Action, Result {

    public RpcWhiteList() {
    }
}
