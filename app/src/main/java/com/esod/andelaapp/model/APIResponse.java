package com.esod.andelaapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jedidiah on 07/03/2017.
 */

public class APIResponse {

    @SerializedName("total_count")
    private int total_count;

    @SerializedName("incomplete_results")
    private boolean incomplete_results;

    @SerializedName("items")
    private Object items;

    public APIResponse(int total_count, boolean incomplete_results, Object items) {
        this.total_count = total_count;
        this.incomplete_results = incomplete_results;
        this.items = items;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }
}
