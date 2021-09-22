package edu.stanford.protege.webprotege.download;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public enum DownloadFormatExtension {

    owl("RDF/XML"),

    ttl("Turtle"),

    owx("OWL/XML"),

    omn("Manchester OWL Syntax"),

    ofn("Functional OWL Syntax");

    private final String displayName;

    DownloadFormatExtension(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExtension() {
        return name();
    }
}
