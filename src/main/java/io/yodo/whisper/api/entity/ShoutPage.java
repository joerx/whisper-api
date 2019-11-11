package io.yodo.whisper.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoutPage {

    @JsonProperty("items")
    private List<Shout> items;

    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    public List<Shout> getItems() {
        return items;
    }

    public void setItems(List<Shout> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
