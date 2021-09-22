package edu.stanford.protege.webprotege.mansyntax.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class LinkInfo {

    private final String linkAddress;

    private final String linkContent;

    public LinkInfo(String linkAddress, String linkContent) {
        this.linkAddress = linkAddress;
        this.linkContent = linkContent;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public String getLinkContent() {
        return linkContent;
    }
}
