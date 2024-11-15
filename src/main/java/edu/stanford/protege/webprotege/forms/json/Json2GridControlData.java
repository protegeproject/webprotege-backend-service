package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.data.*;
import edu.stanford.protege.webprotege.forms.field.GridColumnDescriptor;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Json2GridControlData {

    private final Provider<Json2FormControlData> json2FormDataProvider;

    private final OWLDataFactory dataFactory;

    public Json2GridControlData(Provider<Json2FormControlData> json2FormDataProvider, OWLDataFactory dataFactory) {
        this.json2FormDataProvider = json2FormDataProvider;
        this.dataFactory = dataFactory;
    }


    public List<GridControlData> convert(ArrayNode rowNodes,
                                         GridControlDescriptor descriptor) {
        // Array of rows that are arrays of columns
        var rows = new ArrayList<GridRowData>();
        for (int i = 0; i < rowNodes.size(); i++) {
            var rowNode = rowNodes.get(i);
            if (!rowNode.isObject()) {
                return ImmutableList.of();
            }
            var rowData = convertRowNode(descriptor, rowNode);
            rows.add(rowData);
        }
        var rowsPage = Page.of(ImmutableList.copyOf(rows));
        return ImmutableList.of(GridControlData.get(descriptor, rowsPage, ImmutableSet.of()));
    }

    private GridRowData convertRowNode(GridControlDescriptor descriptor, JsonNode rowNode) {
        var subjectNode = rowNode.get("@id") == null ? null : rowNode.get("@id");
        Optional<FormEntitySubject> entitySubjectEntity = Optional.empty();
        if(subjectNode != null && !subjectNode.isNull() && !subjectNode.asText().isEmpty()) {
            entitySubjectEntity = descriptor.getSubjectFactoryDescriptor()
                    .map(s -> dataFactory.getOWLEntity(s.getEntityType(), IRI.create(subjectNode.asText())))
                    .map(FormEntitySubject::get);
        }

        var colNodes = (ObjectNode) rowNode;
        var rowCellData = new ArrayList<GridCellData>();
        colNodes.remove("@id");

        for (int j = 0; j < colNodes.size(); j++) {
            var colDescriptor = descriptor.getColumns().get(j);
            var colNode = colNodes.get(colDescriptor.getLabel().get("json"));
            var colData = convertColumnNode(colNode, colDescriptor);
            rowCellData.add(colData);
        }
        FormEntitySubject subject = null;
        if(entitySubjectEntity.isPresent()){
            subject = entitySubjectEntity.get();
        }
        return GridRowData.get(subject, ImmutableList.copyOf(rowCellData));
    }

    private GridCellData convertColumnNode(JsonNode colNode, GridColumnDescriptor colDescriptor) {

        if(colNode.isArray()){
            List<? extends FormControlData> formControlDataList = json2FormDataProvider.get().convertFromArray( (ArrayNode) colNode, colDescriptor.getFormControlDescriptor());
            return GridCellData.get(colDescriptor.getId(), Page.of(ImmutableList.copyOf(formControlDataList)));
        } else {
            Optional<? extends FormControlData> formControlData = json2FormDataProvider.get().convert(colNode, colDescriptor.getFormControlDescriptor());
            if (formControlData.isPresent()) {
                return GridCellData.get(colDescriptor.getId(), Page.of(ImmutableList.of(formControlData.get())));
            }
        }

        var values = Page.<FormControlData>emptyPage();
        var columnId = colDescriptor.getId();
        return GridCellData.get(columnId, values);
    }
}
