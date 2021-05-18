package edu.stanford.bmir.protege.web.server.filter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class Filter {

    private final FilterId filterId;

    private final FilterSetting filterSetting;


    public Filter(FilterId filterId, FilterSetting filterSetting) {
        this.filterId = filterId;
        this.filterSetting = filterSetting;
    }

    public FilterId getFilterId() {
        return filterId;
    }

    public FilterSetting getFilterSetting() {
        return filterSetting;
    }
}
