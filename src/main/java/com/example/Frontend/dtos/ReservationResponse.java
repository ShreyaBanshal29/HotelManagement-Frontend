package com.example.Frontend.dtos;

import java.util.List;

import com.example.Frontend.Entities.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationResponse {

    @JsonProperty("content")
    private List<Reservation> content;
    @JsonProperty("_embedded")
    private ReservationEmbedded embedded;

    private PageInfo page;

    public List<Reservation> getContent() {
        if (content != null) {
            return content;
        }
        return embedded != null ? embedded.getReservations() : List.of();
    }

    public void setContent(List<Reservation> content) {
        this.content = content;
    }

    public ReservationEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ReservationEmbedded embedded) {
        this.embedded = embedded;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReservationEmbedded {
        private List<Reservation> reservations;

        public List<Reservation> getReservations() {
            return reservations != null ? reservations : List.of();
        }

        public void setReservations(List<Reservation> reservations) {
            this.reservations = reservations;
        }
    }
}
