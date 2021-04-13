package edu.stanford.bmir.protege.web.shared.pagination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("Page")
public abstract class Page<T> implements Serializable, Iterable<T>, IsSerializable {

    public static <T> Page<T> emptyPage() {
        return create(1, 1, Collections.emptyList(), 0);
    }

    public static Page<FormControlDataDto> of(FormControlDataDto firstValue) {
        return create(1, 1, ImmutableList.of(firstValue), 1);
    }

    public static <T> Page<T> of(ImmutableList<T> values) {
        return create(1, 1, values, values.size());
    }

    @JsonCreator
    public static <T> Page<T> create(@JsonProperty("pageNumber") int pageNumber,
                                     @JsonProperty("pageCount") int pageCount,
                                     @JsonProperty("pageElements") List<T> pageElements,
                                     @JsonProperty("totalElements") long totalElements) {
        checkArgument(pageNumber > 0, "pageNumber must be greater than 0");
        checkArgument(pageCount > 0, "pageCount must be greater than 0");
        checkArgument(!(pageNumber > pageCount));
        checkArgument(totalElements > -1);
        return new AutoValue_Page<T>(ImmutableList.copyOf(pageElements),
                                     pageElements.size(),
                                     totalElements,
                                     pageNumber,
                                     pageCount);
    }

    public abstract ImmutableList<T> getPageElements();

    public abstract int getPageSize();

    public abstract long getTotalElements();

    public abstract int getPageNumber();

    public abstract int getPageCount();

    @Override
    public Iterator<T> iterator() {
        return getPageElements().iterator();
    }

    public <E> Page<E> transform(Function<T, E> function) {
        return create(getPageNumber(),
                      getPageCount(),
                      getPageElements().stream()
                                      .map(function)
                                      .collect(toImmutableList()),
                      getTotalElements());
    }

}
