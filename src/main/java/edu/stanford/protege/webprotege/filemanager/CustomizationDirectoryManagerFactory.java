package edu.stanford.protege.webprotege.filemanager;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/16
 */
public interface CustomizationDirectoryManagerFactory {

    CustomizationDirectoryManager createCustomizationDirectoryManager(File baseDirectory);
}
