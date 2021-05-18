package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 * <p>
 * A dictionary that supports look up for multiple languages
 */
@ProjectSingleton
public interface MultiLingualDictionary extends MultiLingualShortFormDictionary, SearchableMultiLingualShortFormDictionary, MultiLingualShortFormIndex {

}
