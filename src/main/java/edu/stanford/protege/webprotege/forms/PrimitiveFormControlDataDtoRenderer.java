package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.entity.IRIData;
import edu.stanford.protege.webprotege.entity.OWLLiteralData;
import edu.stanford.protege.webprotege.forms.data.IriFormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.LiteralFormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlDataDto;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class PrimitiveFormControlDataDtoRenderer {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Inject
    public PrimitiveFormControlDataDtoRenderer(@Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex, @Nonnull FormDataBuilderSessionRenderer sessionRenderer) {
        this.entitiesInProjectSignatureByIriIndex = checkNotNull(entitiesInProjectSignatureByIriIndex);
        this.sessionRenderer = checkNotNull(sessionRenderer);
    }

    @Nonnull
    public Stream<PrimitiveFormControlDataDto> toFormControlDataDto(@Nonnull OWLPrimitive primitive) {
        if(primitive instanceof IRI) {
            var matchingEntities = entitiesInProjectSignatureByIriIndex.getEntitiesInSignature((IRI) primitive).collect(Collectors.toList());
            if(matchingEntities.isEmpty()) {
                return Stream.of(IriFormControlDataDto.get(IRIData.get((IRI) primitive, ImmutableMap.of())));
            }
            else {
                return matchingEntities.stream().map(sessionRenderer::getEntityRendering)
                        .map(PrimitiveFormControlDataDto::get);
            }
        }
        else if(primitive instanceof OWLLiteral) {
            return Stream.of(LiteralFormControlDataDto.get(OWLLiteralData.get((OWLLiteral) primitive)));
        }
        else if(primitive instanceof OWLEntity) {
            var entityRendering = sessionRenderer.getEntityRendering((OWLEntity) primitive);
            return Stream.of(PrimitiveFormControlDataDto.get(entityRendering));
        }
        else {
            throw new RuntimeException("Cannot handle primitive " + primitive);
        }
    }
}
