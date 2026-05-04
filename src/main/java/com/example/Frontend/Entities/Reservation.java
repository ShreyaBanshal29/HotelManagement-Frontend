package com.example.Frontend.Entities;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {

    private Integer reservationId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room room;
    @JsonProperty("_embedded")
    private ReservationEmbedded embedded;
    @JsonProperty("_links")
    private Map<String, Object> links;

    public Integer getReservationId() {
        if (reservationId == null) {
            reservationId = extractIdFromLinks();
        }
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Room getRoom() {
        if (room != null) {
            return room;
        }
        return embedded != null ? embedded.getRoom() : null;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ReservationEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ReservationEmbedded embedded) {
        this.embedded = embedded;
    }

    public Map<String, Object> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Object> links) {
        this.links = links;
    }

    private Integer extractIdFromLinks() {
        if (links == null) {
            return null;
        }
        Object selfObj = links.get("self");
        if (!(selfObj instanceof Map<?, ?> self)) {
            return null;
        }

        Object hrefObj = self.get("href");
        if (!(hrefObj instanceof String href)) {
            return null;
        }

        String id = href.replaceAll("\\{.*$", "");
        id = id.substring(id.lastIndexOf("/") + 1);
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReservationEmbedded {
        private Room room;

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }
    }
}
