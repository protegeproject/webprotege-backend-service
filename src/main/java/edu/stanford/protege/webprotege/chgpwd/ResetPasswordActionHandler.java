package edu.stanford.protege.webprotege.chgpwd;

import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.auth.Password;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.user.UserId;
import edu.stanford.protege.webprotege.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.chgpwd.ResetPasswordResultCode.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordActionHandler implements ApplicationActionHandler<ResetPasswordAction, ResetPasswordResult> {


    private final UserDetailsManager userDetailsManager;

    private final AuthenticationManager authenticationManager;

    private final ResetPasswordMailer mailer;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordActionHandler.class);


    @Inject
    public ResetPasswordActionHandler(UserDetailsManager userDetailsManager,
                                      AuthenticationManager authenticationManager,
                                      ResetPasswordMailer mailer) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.mailer = mailer;
    }

    @Nonnull
    @Override
    public Class<ResetPasswordAction> getActionClass() {
        return ResetPasswordAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(
            @Nonnull ResetPasswordAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public ResetPasswordResult execute(
            @Nonnull ResetPasswordAction action, @Nonnull ExecutionContext executionContext) {
        final String emailAddress = action.getResetPasswordData().getEmailAddress();
        try {
            Optional<UserId> userId = userDetailsManager.getUserByUserIdOrEmail(emailAddress);
            if(userId.isEmpty()) {
                return ResetPasswordResult.create(INVALID_EMAIL_ADDRESS);
            }
            Optional<UserDetails> userDetails = userDetailsManager.getUserDetails(userId.get());
            if(userDetails.isEmpty()) {
                return ResetPasswordResult.create(INVALID_EMAIL_ADDRESS);
            }
            if(userDetails.get().getEmailAddress().isEmpty()) {
                return ResetPasswordResult.create(INVALID_EMAIL_ADDRESS);
            }
            if(!userDetails.get().getEmailAddress().get().equalsIgnoreCase(emailAddress)) {
                return ResetPasswordResult.create(INVALID_EMAIL_ADDRESS);
            }
            var password = Password.create(IdUtil.getBase62UUID());
            authenticationManager.setPassword(userId.get(), password);
            mailer.sendEmail(userId.get(), emailAddress, password.getPassword(), ex -> {
                throw new RuntimeException(ex);
            });
            logger.info("The password for {} has been reset.  " +
                            "An email has been sent to {} that contains the new password.",
                    userId.get().getUserName(),
                    emailAddress
            );
            return ResetPasswordResult.create(SUCCESS);
        } catch (Exception e) {
            logger.error("Could not reset the user password " +
                                "associated with the email " +
                                "address {}.  The following " +
                                "error occurred: {}.", emailAddress, e.getMessage(), e);
            return ResetPasswordResult.create(INTERNAL_ERROR);
        }
    }
}
