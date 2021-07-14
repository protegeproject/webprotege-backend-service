package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboNamespacesResult implements Result {

    private ImmutableSet<OBONamespace> namespaces;


    private GetOboNamespacesResult() {
    }

    private GetOboNamespacesResult(Set<OBONamespace> namespaces) {
        this.namespaces = ImmutableSet.copyOf(namespaces);
    }

    public static GetOboNamespacesResult create(Set<OBONamespace> namespaces) {
        return new GetOboNamespacesResult(namespaces);
    }

    public Set<OBONamespace> getNamespaces() {
        return namespaces;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(namespaces);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboNamespacesResult)) {
            return false;
        }
        GetOboNamespacesResult other = (GetOboNamespacesResult) obj;
        return this.namespaces.equals(other.namespaces);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboNamespacesResult")
                .addValue(namespaces)
                .toString();
    }
}
