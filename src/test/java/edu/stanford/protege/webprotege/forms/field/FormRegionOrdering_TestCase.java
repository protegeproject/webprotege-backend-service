package edu.stanford.protege.webprotege.forms.field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class FormRegionOrdering_TestCase {

    private FormRegionId columnId;

    private final FormRegionOrderingDirection direction = FormRegionOrderingDirection.ASC;

    private FormRegionOrdering orderBy;

    @BeforeEach
    public void setUp() throws Exception {
        columnId = FormRegionId.generate();
        orderBy = FormRegionOrdering.get(columnId, direction);
    }

    @Test
    public void shouldReturnSuppliedColumnId() {
        assertThat(orderBy.getRegionId(), equalTo(columnId));
    }

    @Test
    public void shouldReturnSuppliedDirection() {
        assertThat(orderBy.getDirection(), equalTo(direction));
    }

    @Test
    public void shouldReturnTrueForAsc() {
        assertThat(orderBy.isAscending(), equalTo(true));
    }

    @Test
    public void shouldReturnFalseForDesc() {
        FormRegionOrdering orderByDesc = FormRegionOrdering.get(columnId, FormRegionOrderingDirection.DESC);
        assertThat(orderByDesc.isAscending(), equalTo(false));
    }
}