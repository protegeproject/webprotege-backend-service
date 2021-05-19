package edu.stanford.protege.webprotege.obo;


import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermCrossProductResult implements Result {

    private OBOTermCrossProduct crossProduct;

    private GetOboTermCrossProductResult(@Nonnull OBOTermCrossProduct crossProduct) {
        this.crossProduct = checkNotNull(crossProduct);
    }


    private GetOboTermCrossProductResult() {
    }

    public static GetOboTermCrossProductResult create(@Nonnull OBOTermCrossProduct crossProduct) {
        return new GetOboTermCrossProductResult(crossProduct);
    }

    @Nonnull
    public OBOTermCrossProduct getCrossProduct() {
        return crossProduct;
    }

    @Override
    public int hashCode() {
        return crossProduct.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermCrossProductResult)) {
            return false;
        }
        GetOboTermCrossProductResult other = (GetOboTermCrossProductResult) obj;
        return this.crossProduct.equals(other.crossProduct);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermCrossProductResult")
                .addValue(crossProduct)
                .toString();
    }
}
