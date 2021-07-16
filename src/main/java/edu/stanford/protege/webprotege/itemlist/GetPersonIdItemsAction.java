package edu.stanford.protege.webprotege.itemlist;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.sharing.PersonId;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 */
public class GetPersonIdItemsAction extends GetItemsAction<PersonId, GetPersonIdItemsResult> {


    public GetPersonIdItemsAction(List<String> itemNames) {
        super(itemNames);
    }

    public static GetPersonIdItemsAction create(List<String> itemNames) {
        return new GetPersonIdItemsAction(itemNames);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItemNames());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdItemsAction)) {
            return false;
        }
        GetPersonIdItemsAction other = (GetPersonIdItemsAction) obj;
        return this.getItemNames().equals(other.getItemNames());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdItemsAction")
                .addValue(getItemNames())
                .toString();
    }
}
