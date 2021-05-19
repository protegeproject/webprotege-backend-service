package edu.stanford.protege.webprotege.crud;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixSettings;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     The settings for an {@link edu.stanford.protege.webprotege.crud.EntityCrudKitHandler}. All subclasses of this class
 *     provide an IRI prefix for entity creation.  Settings specific to each concrete subclass are used to generate an
 *     IRI suffix for an entity.  This suffix may or may not depend upon a supplied short form for the entity.
 * </p>
 */
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = UuidSuffixSettings.class,
                                   name = "edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings"),
                @JsonSubTypes.Type(value = UuidSuffixSettings.class,
                                   name = UuidSuffixSettings.TYPE_ID),
                @JsonSubTypes.Type(value = SuppliedNameSuffixSettings.class,
                                   name = "edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixSettings"),
                @JsonSubTypes.Type(value = SuppliedNameSuffixSettings.class,
                                   name = SuppliedNameSuffixSettings.TYPE_ID),
                @JsonSubTypes.Type(value = OboIdSuffixSettings.class,
                                   name = "edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings"),
                @JsonSubTypes.Type(value = OboIdSuffixSettings.class,
                                   name = OboIdSuffixSettings.TYPE_ID)
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = EntityCrudKitSuffixSettings.TYPE_PROPERTY)
public abstract class EntityCrudKitSuffixSettings implements HasKitId, Serializable {

        /**
         * The type property.  This is _class for backwards compatibility
         */
        public static final String TYPE_PROPERTY = "_class";
}
