package edu.stanford.bmir.protege.web.server.form;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public
interface HasFormRegionPagedChangedHandler {

    void setRegionPageChangedHandler(@Nonnull RegionPageChangedHandler handler);
}
