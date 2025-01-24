package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.mail.CommentMessageIdGenerator;
import edu.stanford.protege.webprotege.mail.MessageHeader;
import edu.stanford.protege.webprotege.mail.MessageId;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_OBJECT_COMMENT;
import static edu.stanford.protege.webprotege.authorization.ProjectResource.forProject;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class CommentNotificationEmailer {

    private final ProjectDetailsManager projectDetailsManager;

    private final UserDetailsManager userDetailsManager;

    private final AccessManager accessManager;

    private final SendMail sendMail;

    private final DiscussionThreadParticipantsExtractor participantsExtractor;

    private final CommentNotificationEmailGenerator emailGenerator;

    private final CommentMessageIdGenerator messageIdGenerator;

    @Inject
    public CommentNotificationEmailer(@Nonnull ProjectDetailsManager projectDetailsManager,
                                      @Nonnull UserDetailsManager userDetailsManager,
                                      @Nonnull AccessManager accessManager,
                                      @Nonnull DiscussionThreadParticipantsExtractor participantsExtractor,
                                      @Nonnull CommentNotificationEmailGenerator emailGenerator,
                                      @Nonnull SendMail sendMail,
                                      @Nonnull CommentMessageIdGenerator messageIdGenerator) {
        this.messageIdGenerator = checkNotNull(messageIdGenerator);
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
        this.userDetailsManager = checkNotNull(userDetailsManager);
        this.accessManager = checkNotNull(accessManager);
        this.sendMail = checkNotNull(sendMail);
        this.participantsExtractor = checkNotNull(participantsExtractor);
        this.emailGenerator = checkNotNull(emailGenerator);
    }

    public void sendCommentPostedNotification(@Nonnull ProjectId projectId,
                                              @Nonnull OWLEntityData entityData,
                                              @Nonnull EntityDiscussionThread thread,
                                              @Nonnull Comment postedComment) {
        Collection<UserDetails> userDetails = getParticipantUserDetails(projectId, postedComment, thread);
        sendEmailToUsers(userDetails, projectId, entityData, thread, postedComment);
    }

    private Collection<UserDetails> getParticipantUserDetails(@Nonnull ProjectId projectId,
                                                              @Nonnull Comment postedComment,
                                                              @Nonnull EntityDiscussionThread thread) {
        return getUsersToNotify(projectId, postedComment, thread).stream()
                                                                 .map(userDetailsManager::getUserDetails)
                                                                 .filter(Optional::isPresent)
                                                                 .map(Optional::get)
                                                                 .collect(toList());
    }

    private Collection<UserId> getUsersToNotify(@Nonnull ProjectId projectId,
                                                @Nonnull Comment postedComment,
                                                @Nonnull EntityDiscussionThread thread) {
        // Thread participants that can view comments
        var threadParticipants = participantsExtractor.extractParticipants(thread).stream()
                .filter(userId -> accessManager.hasPermission(forUser(userId),
                                                              forProject(projectId),
                                                              VIEW_OBJECT_COMMENT));
        // Any users that can view comments on the project
        var projectParticipants = accessManager.getSubjectsWithAccessToResource(forProject(projectId),
                                                                                VIEW_OBJECT_COMMENT)
                                               .stream()
                                               .map(Subject::getUserId)
                                               .filter(Optional::isPresent)
                                               .map(Optional::get)
                                               // Can't send out notifications to guests
                                               .filter(userId -> !userId.isGuest());
        return Stream.concat(threadParticipants, projectParticipants)
                     // Don't send notifications to the person who posted the comment
                     .filter(userId -> userIsNotCommentAuthor(userId, postedComment)).collect(toSet());
    }

    private static boolean userIsNotCommentAuthor(@Nonnull UserId userId, @Nonnull Comment postedComment) {
        return !userId.equals(postedComment.getCreatedBy());
    }


    private void sendEmailToUsers(@Nonnull Collection<UserDetails> userDetails,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull OWLEntityData entityData,
                                  @Nonnull EntityDiscussionThread discussionThread,
                                  @Nonnull Comment postedComment) {
        // Don't send anything if there's nobdoy to send it to!
        if (userDetails.isEmpty()) {
            return;
        }
        var emailAddresses = userDetails.stream()
                                        .map(UserDetails::getEmailAddress)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(toList());
        MessageId postedCommentMessageId = messageIdGenerator.generateCommentMessageId(projectId,
                                                                                       postedComment.getId());
        List<MessageHeader> messageHeaders = new ArrayList<>();
        int commentIndex = discussionThread.getComments().indexOf(postedComment);
        if (commentIndex != 0) {
            // Reply to the original message
            MessageId headCommentMessageId = messageIdGenerator.generateCommentMessageId(projectId,
                                                                                         discussionThread.getComments()
                                                                                                         .get(0)
                                                                                                         .getId());
            messageHeaders.add(MessageHeader.inReplyTo(headCommentMessageId.getId()));
            messageHeaders.add(MessageHeader.references(headCommentMessageId.getId()));
        }
        sendMail.sendMail(postedCommentMessageId,
                          emailAddresses,
                          formatSubjectLine(projectId, entityData, discussionThread, postedComment),
                          formatMessage(projectId, entityData, discussionThread, postedComment),
                          messageHeaders.toArray(new MessageHeader[messageHeaders.size()]));
    }


    private String formatSubjectLine(@Nonnull ProjectId projectId,
                                     @Nonnull OWLEntityData entityData,
                                     @Nonnull EntityDiscussionThread thread,
                                     @Nonnull Comment postedComment) {
        return String.format("[%s] Comment posted by %s",
                             projectDetailsManager.getProjectDetails(projectId).getDisplayName(),
                             postedComment.getCreatedBy().id());
    }

    private String formatMessage(@Nonnull ProjectId projectId,
                                 @Nonnull OWLEntityData entityData,
                                 @Nonnull EntityDiscussionThread thread,
                                 @Nonnull Comment postedComment) {
        String projectName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
        return emailGenerator.generateEmailBody(projectName, entityData, thread, postedComment);
    }

}
