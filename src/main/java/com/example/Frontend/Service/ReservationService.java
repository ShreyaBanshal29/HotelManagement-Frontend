package com.example.Frontend.Service;

import java.net.URI;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.Entities.Reservation;
import com.example.Frontend.Entities.Room;
import com.example.Frontend.Entities.RoomType;
import com.example.Frontend.dtos.PageInfo;
import com.example.Frontend.dtos.ReservationFormDto;
import com.example.Frontend.dtos.ReservationResponse;
import com.example.Frontend.dtos.RoomPagedModel;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ReservationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ReservationResponse getReservations(int page, String filterBy, String keyword) {
        UriComponentsBuilder builder;

        if (keyword != null && !keyword.trim().isEmpty() && filterBy != null && !filterBy.isBlank()) {
            builder = UriComponentsBuilder
                    .fromUriString(baseUrl + "/reservations/search/" + searchMethod(filterBy))
                    .queryParam(filterParam(filterBy), keyword.trim());
        } else {
            builder = UriComponentsBuilder.fromUriString(baseUrl + "/reservations");
        }

        URI uri = builder
                .queryParam("page", page)
                .queryParam("size", 10)
                .build()
                .encode()
                .toUri();

        return mapReservationResponse(getJsonText(uri));
    }

    public Reservation getReservationById(Integer id) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/reservations/" + id)
                .build()
                .toUri();

        Reservation reservation = mapReservation(getJsonNode(uri));
        if (reservation != null && reservation.getRoom() == null) {
            reservation.setRoom(getRoomByReservationId(id));
        }
        return reservation;
    }

    public List<Room> getRooms() {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/room")
                .queryParam("size", 100)
                .queryParam("page", 0)
                .build()
                .toUri();

        RoomPagedModel model = restTemplate.getForObject(uri, RoomPagedModel.class);
        return model != null ? model.getRooms() : List.of();
    }

    public Room getRoomById(Integer id) {
        if (id == null) {
            return null;
        }

        return restTemplate.getForObject(baseUrl + "/room/" + id, Room.class);
    }

    public Room getRoomByReservationId(Integer reservationId) {
        if (reservationId == null) {
            return null;
        }

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/reservations/" + reservationId + "/room")
                .build()
                .toUri();

        return mapRoom(getJsonNode(uri));
    }

    public void createReservation(ReservationFormDto form) {
        postOrPut(baseUrl + "/reservations", HttpMethod.POST, form, form.getRoomId());
    }

    public void updateReservation(Integer id, ReservationFormDto form) {
        Reservation existing = getReservationById(id);
        Integer originalRoomId = existing != null && existing.getRoom() != null
                ? existing.getRoom().getRoomId()
                : form.getRoomId();

        postOrPut(baseUrl + "/reservations/" + id, HttpMethod.PUT, form, originalRoomId);
    }

    public ReservationFormDto toForm(Reservation reservation) {
        ReservationFormDto form = new ReservationFormDto();
        form.setGuestName(reservation.getGuestName());
        form.setGuestEmail(reservation.getGuestEmail());
        form.setGuestPhone(reservation.getGuestPhone());
        form.setCheckInDate(reservation.getCheckInDate());
        form.setCheckOutDate(reservation.getCheckOutDate());
        if (reservation.getRoom() != null) {
            form.setRoomId(reservation.getRoom().getRoomId());
        }
        return form;
    }

    private void postOrPut(String url, HttpMethod method, ReservationFormDto form, Integer roomId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("guestName", form.getGuestName());
        requestBody.put("guestEmail", form.getGuestEmail());
        requestBody.put("guestPhone", form.getGuestPhone());
        requestBody.put("checkInDate", formatDate(form.getCheckInDate()));
        requestBody.put("checkOutDate", formatDate(form.getCheckOutDate()));
        requestBody.put("room", baseUrl + "/room/" + roomId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(url, method, entity, Void.class);
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : null;
    }

    private String searchMethod(String filterBy) {
        return switch (filterBy) {
            case "guestPhone" -> "findByGuestPhone";
            case "guestEmail" -> "findByGuestEmail";
            case "checkInDate" -> "findByCheckInDate";
            case "checkOutDate" -> "findByCheckOutDate";
            case "roomId" -> "findByRoom_RoomId";
            default -> "findByGuestName";
        };
    }

    private String filterParam(String filterBy) {
        return switch (filterBy) {
            case "guestPhone" -> "guestPhone";
            case "guestEmail" -> "guestEmail";
            case "checkInDate" -> "checkInDate";
            case "checkOutDate" -> "checkOutDate";
            case "roomId" -> "roomId";
            default -> "guestName";
        };
    }

    @SuppressWarnings("unchecked")
    private ReservationResponse mapReservationResponse(Map<String, Object> response) {
        ReservationResponse reservationResponse = new ReservationResponse();
        if (response == null) {
            reservationResponse.setContent(List.of());
            return reservationResponse;
        }

        Map<String, Object> embedded = (Map<String, Object>) response.get("_embedded");
        List<Map<String, Object>> rawReservations = embedded != null
                ? (List<Map<String, Object>>) embedded.getOrDefault("reservations", Collections.emptyList())
                : (List<Map<String, Object>>) response.getOrDefault("content", Collections.emptyList());

        reservationResponse.setContent(rawReservations.stream()
                .map(this::mapReservation)
                .toList());

        Map<String, Object> rawPage = (Map<String, Object>) response.get("page");
        if (rawPage != null) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setNumber(toInt(rawPage.get("number")));
            pageInfo.setSize(toInt(rawPage.get("size")));
            pageInfo.setTotalElements(toInt(rawPage.get("totalElements")));
            pageInfo.setTotalPages(toInt(rawPage.get("totalPages")));
            reservationResponse.setPage(pageInfo);
        }

        return reservationResponse;
    }

    @SuppressWarnings("unchecked")
    private Reservation mapReservation(Map<String, Object> raw) {
        if (raw == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setReservationId(extractId(raw));
        reservation.setGuestName((String) raw.get("guestName"));
        reservation.setGuestEmail((String) raw.get("guestEmail"));
        reservation.setGuestPhone((String) raw.get("guestPhone"));
        reservation.setCheckInDate(toDate(raw.get("checkInDate")));
        reservation.setCheckOutDate(toDate(raw.get("checkOutDate")));

        Map<String, Object> embedded = (Map<String, Object>) raw.get("_embedded");
        if (embedded != null) {
            reservation.setRoom(mapRoom((Map<String, Object>) embedded.get("room")));
        }

        if (reservation.getRoom() == null && reservation.getReservationId() != null) {
            reservation.setRoom(getRoomByReservationId(reservation.getReservationId()));
        }

        return reservation;
    }

    @SuppressWarnings("unchecked")
    private Room mapRoom(Map<String, Object> raw) {
        if (raw == null) {
            return null;
        }

        Room room = new Room();
        room.setRoomId(toInt(raw.get("roomId")));
        room.setRoomNumber(toInt(raw.get("roomNumber")));
        room.setIsAvailable((Boolean) raw.get("isAvailable"));
        room.setHotel(mapHotel((Map<String, Object>) raw.get("hotel")));
        room.setRoomType(mapRoomType((Map<String, Object>) raw.get("roomType")));
        return room;
    }

    private Hotel mapHotel(Map<String, Object> raw) {
        if (raw == null) {
            return null;
        }

        Hotel hotel = new Hotel();
        hotel.setHotelId(extractId(raw));
        hotel.setName((String) raw.get("name"));
        hotel.setLocation((String) raw.get("location"));
        hotel.setDescription((String) raw.get("description"));
        return hotel;
    }

    private RoomType mapRoomType(Map<String, Object> raw) {
        if (raw == null) {
            return null;
        }

        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(extractId(raw));
        roomType.setTypeName((String) raw.get("typeName"));
        roomType.setDescription((String) raw.get("description"));
        roomType.setMaxOccupancy(toInt(raw.get("maxOccupancy")));
        Object price = raw.get("pricePerNight");
        if (price instanceof Number number) {
            roomType.setPricePerNight(BigDecimal.valueOf(number.doubleValue()));
        }
        return roomType;
    }

    @SuppressWarnings("unchecked")
    private Integer extractId(Map<String, Object> raw) {
        Object linksObj = raw.get("_links");
        if (!(linksObj instanceof Map<?, ?> links)) {
            return null;
        }

        Object selfObj = ((Map<String, Object>) links).get("self");
        if (!(selfObj instanceof Map<?, ?> self)) {
            return null;
        }

        Object hrefObj = ((Map<String, Object>) self).get("href");
        if (!(hrefObj instanceof String href)) {
            return null;
        }

        String cleanHref = href.replaceAll("\\{.*$", "");
        String id = cleanHref.substring(cleanHref.lastIndexOf("/") + 1);
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private LocalDate toDate(Object value) {
        return value instanceof String text && !text.isBlank() ? LocalDate.parse(text) : null;
    }

    private int toInt(Object value) {
        return value instanceof Number number ? number.intValue() : 0;
    }

    private ReservationResponse mapReservationResponse(JsonNode root) {
        ReservationResponse response = new ReservationResponse();
        if (root == null || root.isMissingNode()) {
            response.setContent(List.of());
            return response;
        }

        JsonNode reservationsNode = root.path("_embedded").path("reservations");
        if (reservationsNode.isMissingNode() || !reservationsNode.isArray()) {
            response.setContent(List.of());
        } else {
            response.setContent(reservationsNode.valueStream()
                    .map(this::mapReservation)
                    .toList());
        }

        JsonNode pageNode = root.path("page");
        if (!pageNode.isMissingNode()) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setNumber(pageNode.path("number").asInt(0));
            pageInfo.setSize(pageNode.path("size").asInt(0));
            pageInfo.setTotalElements(pageNode.path("totalElements").asInt(0));
            pageInfo.setTotalPages(pageNode.path("totalPages").asInt(0));
            response.setPage(pageInfo);
        }

        return response;
    }

    private ReservationResponse mapReservationResponse(String json) {
        ReservationResponse response = new ReservationResponse();
        JsonNode root = parseJson(json);
        JsonNode reservationsNode = root.path("_embedded").path("reservations");

        if (!reservationsNode.isMissingNode() && reservationsNode.isArray()) {
            List<Reservation> reservations = new java.util.ArrayList<>();
            List<Integer> ids = extractReservationIds(json);
            int index = 0;
            for (JsonNode reservationNode : reservationsNode) {
                Reservation reservation = mapReservation(reservationNode);
                if (reservation != null && reservation.getReservationId() == null && index < ids.size()) {
                    reservation.setReservationId(ids.get(index));
                }
                reservations.add(reservation);
                index++;
            }
            response.setContent(reservations);
        } else {
            response.setContent(extractReservationIds(json).stream()
                    .limit(10)
                    .map(this::getReservationById)
                    .toList());
        }

        JsonNode pageNode = root.path("page");
        if (!pageNode.isMissingNode()) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setNumber(pageNode.path("number").asInt(0));
            pageInfo.setSize(pageNode.path("size").asInt(0));
            pageInfo.setTotalElements(pageNode.path("totalElements").asInt(0));
            pageInfo.setTotalPages(pageNode.path("totalPages").asInt(0));
            response.setPage(pageInfo);
        }

        return response;
    }

    private Reservation mapReservation(JsonNode node) {
        if (node == null || node.isMissingNode()) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setReservationId(extractId(node));
        if (reservation.getReservationId() == null) {
            List<Integer> ids = extractReservationIds(node.toString());
            if (!ids.isEmpty()) {
                reservation.setReservationId(ids.get(0));
            }
        }
        reservation.setGuestName(text(node.path("guestName")));
        reservation.setGuestEmail(text(node.path("guestEmail")));
        reservation.setGuestPhone(text(node.path("guestPhone")));
        reservation.setCheckInDate(toDate(text(node.path("checkInDate"))));
        reservation.setCheckOutDate(toDate(text(node.path("checkOutDate"))));
        reservation.setRoom(mapRoom(node.path("_embedded").path("room")));

        if (reservation.getRoom() == null && reservation.getReservationId() != null) {
            reservation.setRoom(getRoomByReservationId(reservation.getReservationId()));
        }

        return reservation;
    }

    private Room mapRoom(JsonNode node) {
        if (node == null || node.isMissingNode()) {
            return null;
        }

        Room room = new Room();
        room.setRoomId(node.path("roomId").asInt(0));
        room.setRoomNumber(node.path("roomNumber").asInt(0));
        room.setIsAvailable(node.path("isAvailable").asBoolean(false));
        room.setHotel(mapHotel(node.path("hotel")));
        room.setRoomType(mapRoomType(node.path("roomType")));
        return room;
    }

    private Hotel mapHotel(JsonNode node) {
        if (node == null || node.isMissingNode()) {
            return null;
        }

        Hotel hotel = new Hotel();
        hotel.setHotelId(extractId(node));
        hotel.setName(text(node.path("name")));
        hotel.setLocation(text(node.path("location")));
        hotel.setDescription(text(node.path("description")));
        return hotel;
    }

    private RoomType mapRoomType(JsonNode node) {
        if (node == null || node.isMissingNode()) {
            return null;
        }

        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(extractId(node));
        roomType.setTypeName(text(node.path("typeName")));
        roomType.setDescription(text(node.path("description")));
        roomType.setMaxOccupancy(node.path("maxOccupancy").asInt(0));
        roomType.setPricePerNight(BigDecimal.valueOf(node.path("pricePerNight").asDouble(0)));
        return roomType;
    }

    private Integer extractId(JsonNode node) {
        String href = text(node.path("_links").path("self").path("href"));
        if (href == null || href.isBlank()) {
            return null;
        }

        String cleanHref = href.replaceAll("\\{.*$", "");
        String id = cleanHref.substring(cleanHref.lastIndexOf("/") + 1);
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String text(JsonNode node) {
        return node == null || node.isMissingNode() ? null : node.asText();
    }

    private LocalDate toDate(String value) {
        return value != null && !value.isBlank() ? LocalDate.parse(value) : null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonMap(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        String json;
        try {
            json = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new IllegalStateException("Could not read backend response.", e);
        }

        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not parse backend response.", e);
        }
    }

    private JsonNode getJsonNode(URI uri) {
        return parseJson(getJsonText(uri));
    }

    private String getJsonText(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new IllegalStateException("Could not read backend response.", e);
        }
    }

    private JsonNode parseJson(String json) {
        if (json == null || json.isBlank()) {
            return objectMapper.nullNode();
        }

        try {
            return objectMapper.readTree(json);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not parse backend response.", e);
        }
    }

    private List<Integer> extractReservationIds(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        Pattern pattern = Pattern.compile("/reservations/(\\d+)");
        Matcher matcher = pattern.matcher(json);
        Set<Integer> ids = new LinkedHashSet<>();
        while (matcher.find()) {
            ids.add(Integer.parseInt(matcher.group(1)));
        }
        return ids.stream().toList();
    }
}
