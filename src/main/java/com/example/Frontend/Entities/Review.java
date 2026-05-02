package com.example.Frontend.Entities;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Review {

	private Integer rating;
	private String comment;
	private LocalDate reviewDate;
	private String guestName;
	private String hotelName;

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDate getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(LocalDate reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public Review() {
	}

	public Review(Integer rating, String comment, LocalDate reviewDate, String guestName, String hotelName) {
		this.rating = rating;
		this.comment = comment;
		this.reviewDate = reviewDate;
		this.guestName = guestName;
		this.hotelName = hotelName;
	}
	
}