package com.example.Frontend.dtos;

import com.example.Frontend.Entities.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("page")
    private PageInfo page;

    public List<Review> getContent() {
        if (embedded != null && embedded.getReviews() != null) {
            return embedded.getReviews();
        }
        return new ArrayList<>();
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        @JsonProperty("reviews")
        private List<Review> reviews;

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }
    }
}