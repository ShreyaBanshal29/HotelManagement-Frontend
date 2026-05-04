package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Frontend.Entities.Review;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8081/api/reviews";

    public List<Review> getReviewsByHotelId(Integer hotelId) {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/search/byHotel")
                .queryParam("hotelId", hotelId)
                .queryParam("projection", "reviewSummary")
                .build()
                .encode()
                .toUriString();
        
        System.out.println("Fetching reviews from: " + url);
        
        try {
            String rawJson = restTemplate.getForObject(url, String.class);
            System.out.println("Raw JSON: " + rawJson);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            
            List<Review> reviews = new ArrayList<>();
            
            // Get the content array directly
            JsonNode content = root.get("content");
            if (content != null && content.isArray()) {
                for (JsonNode reviewNode : content) {
                    Review review = new Review();
                    
                    if (reviewNode.has("rating")) {
                        review.setRating(reviewNode.get("rating").asInt());
                    }
                    if (reviewNode.has("comment")) {
                        review.setComment(reviewNode.get("comment").asText());
                    }
                    if (reviewNode.has("guestName")) {
                        review.setGuestName(reviewNode.get("guestName").asText());
                    }
                    if (reviewNode.has("hotelName")) {
                        review.setHotelName(reviewNode.get("hotelName").asText());
                    }
                    if (reviewNode.has("reviewDate")) {
                        String dateStr = reviewNode.get("reviewDate").asText();
                        review.setReviewDate(LocalDate.parse(dateStr));
                    }
                    
                    reviews.add(review);
                    System.out.println("Added review: " + review.getComment());
                }
            }
            
            System.out.println("Successfully parsed " + reviews.size() + " reviews");
            return reviews;
            
        } catch (Exception e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}