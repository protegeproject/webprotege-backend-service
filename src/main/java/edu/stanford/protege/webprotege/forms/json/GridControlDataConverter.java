package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.GridControlData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import jakarta.inject.Provider;

public class GridControlDataConverter {

    private final static String JSON_LANG = "json";

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final Provider<FormControlDataConverter> formControlDataConverterProvider;

    public GridControlDataConverter(Provider<FormControlDataConverter> formControlDataConverterProvider) {
        this.formControlDataConverterProvider = formControlDataConverterProvider;
    }

    @Nonnull
    public JsonNode convert(GridControlData gridControlData) {
        var r = nodeFactory.arrayNode();
        // Array of rows
        var descriptor = gridControlData.getDescriptor();
        var columnDescriptors = descriptor.getColumns();

        gridControlData.getRows()
                .getPageElements()
                .stream()
                .map(row -> {
                    var cells = row.getCells();
                    var rowNode = nodeFactory.objectNode();
                    if(row.getSubject().isPresent()){
                        rowNode.set("@id", new TextNode(row.getSubject().get().getIri().toString()));
                    }
                    for (int i = 0; i < columnDescriptors.size(); i++) {
                        var columnDescriptor = columnDescriptors.get(i);
                        var columnKey = columnDescriptor.getLabel().asMap().getOrDefault(JSON_LANG, "col" + i);
                        var cell = cells.get(i);
                        if (columnDescriptor.isRepeatable()) {
                            var cellValuesArray = nodeFactory.arrayNode();
                            cell.getValues()
                                    .getPageElements()
                                    .stream()
                                    .map(this::convertFormControlData)
                                    .forEach(cellValuesArray::add);
                            rowNode.set(columnKey, cellValuesArray);
                        } else {
                            cell.getValues()
                                    .getPageElements()
                                    .stream()
                                    .findFirst()
                                    .map(this::convertFormControlData)
                                    .ifPresent(cellNode -> rowNode.set(columnKey, cellNode));
                        }
                    }
                    return rowNode;
                }).forEach(r::add);
        return r;
    }

    private JsonNode convertFormControlData(@NotNull FormControlData formControlData) {
        var converter = formControlDataConverterProvider.get();
        return converter.convert(formControlData);
    }
}
