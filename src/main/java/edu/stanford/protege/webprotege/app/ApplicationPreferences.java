package edu.stanford.protege.webprotege.app;

import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Mar 2017
 *
 * An object that holds application preferences that are stored in the database.
 */
//@Entity(cap = @CappedAt(count = 1L), noClassnameStored = true)
@Document(collection = "ApplicationPreferences")
public class ApplicationPreferences {

    public static final String ID = "Preferences";

    @SuppressWarnings("unused")
    private final String id = ID;

    private final String applicationName;

    private final String systemNotificationEmailAddress;

    private final ApplicationLocation applicationLocation;

    private final long maxUploadSize;
    
    public ApplicationPreferences(String applicationName,
                                  String systemNotificationEmailAddress,
                                  ApplicationLocation applicationLocation,
                                  long maxUploadSize) {
        this.applicationName = applicationName == null ? "ICAT-X" : applicationName;
        this.systemNotificationEmailAddress = systemNotificationEmailAddress == null ? "dummy@email.com" : systemNotificationEmailAddress;
        this.applicationLocation = applicationLocation == null ? new ApplicationLocation("http","webprotege-local.edu", "/webprotege", 8080) : applicationLocation;
        this.maxUploadSize = maxUploadSize;
    }

    /**
     * Gets the application name.
     * @return A string representing the application name.
     */
    @Nonnull
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Gets the administrator's email address.
     * @return A string representing the admins email address.  May be empty.
     */
    @Nonnull
    public String getSystemNotificationEmailAddress() {
        return systemNotificationEmailAddress;
    }

    /**
     * Get the application location.
     * @return The location of the application.  This is used in links in notification emails.
     */
    @Nonnull
    public ApplicationLocation getApplicationLocation() {
        return applicationLocation;
    }

    /**
     * Gets the maximum file upload size.
     * @return The maximum file upload size.
     */
    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicationName,
                systemNotificationEmailAddress,
                                applicationLocation,
                                maxUploadSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationPreferences)) {
            return false;
        }
        ApplicationPreferences other = (ApplicationPreferences) obj;
        return this.applicationName.equals(other.applicationName)
                && this.systemNotificationEmailAddress.equals(other.systemNotificationEmailAddress)
                && this.applicationLocation.equals(other.applicationLocation)
                && this.maxUploadSize == other.maxUploadSize;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationPreferences" )
                .addValue(applicationName)
                .add("systemNotificationEmail", systemNotificationEmailAddress)
                .addValue(applicationLocation)
                .toString();
    }
}
