package edu.stanford.protege.webprotege.mansyntax.render;


import jakarta.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class MarkdownLiteralRenderer implements LiteralRenderer {

    @Inject
    public MarkdownLiteralRenderer() {
    }

    @Override
    public void renderLiteral(String literal, StringBuilder stringBuilder) {
        // TODO:  Won't work until we move to Java 7
        stringBuilder.append(literal);
//        try {
//            stringBuilder.append(Processor.process(literal));
//            Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
//            String rendering = markdown4jProcessor.process(literal);
//            stringBuilder.append(rendering.trim());
//        } catch (IOException e) {
//            stringBuilder.append(literal);
//        }
    }
}
