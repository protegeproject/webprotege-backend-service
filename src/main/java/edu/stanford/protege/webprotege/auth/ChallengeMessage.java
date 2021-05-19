package edu.stanford.protege.webprotege.auth;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;


import java.util.Arrays;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChallengeMessage {

    private byte [] message;

    /**
     * For serialization purposes only
     */
    private ChallengeMessage() {
    }

    public ChallengeMessage(byte[] messageBytes) {
        this.message = Arrays.copyOf(checkNotNull(messageBytes), messageBytes.length);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(message, message.length);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Arrays.hashCode(message));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChallengeMessage)) {
            return false;
        }
        ChallengeMessage other = (ChallengeMessage) obj;
        return Arrays.equals(this.message, other.message);
    }


    @Override
    public String toString() {
        return toStringHelper("ChallengeMessage")
                .addValue(BaseEncoding.base16().lowerCase().encode(message))
                .toString();
    }
}
