package edu.stanford.protege.webprotege.perspective;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public interface NodeVisitor<O> {

    O visit(TerminalNode terminalNode);

    O visit(ParentNode parentNode);

}