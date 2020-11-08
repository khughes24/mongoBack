package com.pluralsight.model;

import java.util.List;

public class ActivitySearch {

    private ActivitySearchType searchType;

    public int getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(int durationFrom) {
        this.durationFrom = durationFrom;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    private  int durationFrom;

    public int getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(int durationTo) {
        this.durationTo = durationTo;
    }

    private int durationTo;

    private List<String> descriptions;



}
