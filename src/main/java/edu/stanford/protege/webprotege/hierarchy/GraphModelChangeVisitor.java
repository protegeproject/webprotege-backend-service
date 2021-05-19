package edu.stanford.protege.webprotege.hierarchy;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public interface GraphModelChangeVisitor<U> {

    void visit(AddRootNode<U> addRootNode);

    void visit(RemoveRootNode<U> removeRootNode);

    void visit(AddEdge<U> addEdge);

    void visit(RemoveEdge<U> removeEdge);

    void visit(UpdateUserObject<U> updateUserObject);
}
