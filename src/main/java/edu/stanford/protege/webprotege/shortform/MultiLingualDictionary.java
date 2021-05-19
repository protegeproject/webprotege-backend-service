package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;

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
