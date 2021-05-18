package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.server.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.server.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@ApplicationSingleton
public class SuppliedNameSuffixKit extends EntityCrudKit<SuppliedNameSuffixSettings> {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    @Inject
    public SuppliedNameSuffixKit() {
        super(ID, "Supplied name");
    }

    public static EntityCrudKitId getId() {
        return ID;
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get();
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSuffixSettings() {
        return SuppliedNameSuffixSettings.get();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(!(prefix.endsWith("#") || prefix.endsWith("/"))) {
            return Optional.of("It is recommended that your prefix ends with a forward slash i.e. <b>/</b> (or a #)");
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, SuppliedNameSuffixSettings suffixSettings) {
        return IRI.create(prefixSettings.getIRIPrefix(), "Person");
    }

    @Override
    public GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}
