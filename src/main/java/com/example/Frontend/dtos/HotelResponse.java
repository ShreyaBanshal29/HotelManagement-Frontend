package com.example.Frontend.dtos;

import com.example.Frontend.Entities.Hotel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelResponse {

	@JsonProperty("_embedded")
	private Embedded embedded;

	@JsonProperty("content")
	private List<Hotel> content;

	private Page page;

	public List<Hotel> getContent() {
		if (content != null && !content.isEmpty()) {
			return content;
		}
		if (embedded != null && embedded.getHotels() != null && !embedded.getHotels().isEmpty()) {
			return embedded.getHotels();
		}
		return Collections.emptyList();
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setEmbedded(Embedded embedded) {
		this.embedded = embedded;
	}

	public Embedded getEmbedded() {
		return embedded;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Embedded {
		@JsonProperty("hotels")
		private List<Hotel> hotels;

		public List<Hotel> getHotels() {
			return hotels;
		}

		public void setHotels(List<Hotel> hotels) {
			this.hotels = hotels;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Page {
		private int number;
		private int size;
		private long totalElements;
		private int totalPages;

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public long getTotalElements() {
			return totalElements;
		}

		public void setTotalElements(long totalElements) {
			this.totalElements = totalElements;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(int totalPages) {
			this.totalPages = totalPages;
		}
	}
}