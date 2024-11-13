package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import java.util.Optional;

public class Json2GridControlData {

    private final Provider<Json2FormData> json2FormDataProvider;

    private final OWLDataFactory dataFactory;

    public Json2GridControlData(Provider<Json2FormData> json2FormDataProvider, OWLDataFactory dataFactory) {
        this.json2FormDataProvider = json2FormDataProvider;
        this.dataFactory = dataFactory;
    }


    public Optional<GridControlData> convert(JsonNode node,
                                             GridControlDescriptor descriptor) {
        // Array of rows that are arrays of columns
        if(!node.isArray()) {
            return Optional.empty();
        }
        var rowNodes = (ArrayNode) node;
        var rows = new ArrayList<GridRowData>();
        for(int i = 0; i < rowNodes.size(); i++) {
            var rowNode = rowNodes.get(i);
            if(!rowNode.isArray()) {
                return Optional.empty();
            }
            var rowData = convertRowNode(descriptor, rowNode);
            rows.add(rowData);
        }
        var rowsPage = Page.of(ImmutableList.copyOf(rows));
        return Optional.of(GridControlData.get(descriptor, rowsPage, ImmutableSet.of()));

    }

    private GridRowData convertRowNode(GridControlDescriptor descriptor, JsonNode rowNode) {
        var subjectNode = rowNode.get("@id");
        var entitySubjectEntity = descriptor.getSubjectFactoryDescriptor()
                .map(s -> dataFactory.getOWLEntity(s.getEntityType(), IRI.create(subjectNode.asText())))
                .map(FormEntitySubject::get);

        var colNodes = (ArrayNode) rowNode;
        var rowCellData = new ArrayList<GridCellData>();
        for(int j = 0; j < colNodes.size(); j++) {
            var colDescriptor = descriptor.getColumns().get(j);
            var colNode = colNodes.get(j);
            var colData = convertColumnNode(colNode, colDescriptor);
            rowCellData.add(colData);
        }
        return GridRowData.get(entitySubjectEntity.orElse(null), ImmutableList.copyOf(rowCellData));
    }

    private static GridCellData convertColumnNode(JsonNode colNode, GridColumnDescriptor colDescriptor) {
        if(colNode.isArray()) {
            var values = Page.<FormControlData>emptyPage();
            var columnId = colDescriptor.getId();
            return GridCellData.get(columnId, values);
        }
        else {
            var values = Page.<FormControlData>emptyPage();
            var columnId = colDescriptor.getId();
            return GridCellData.get(columnId, values);
        }
    }
}
