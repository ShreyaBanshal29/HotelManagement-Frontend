package com.example.Frontend.Entities;

import java.math.BigDecimal;


public class RoomType {

	    private Integer roomTypeId;
	    private String typeName;
	    private String description;
	    private Integer maxOccupancy;
	    private BigDecimal pricePerNight;


		public Integer getRoomTypeId() {
			return roomTypeId;
		}

		public void setRoomTypeId(Integer roomTypeId) {
			this.roomTypeId = roomTypeId;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getMaxOccupancy() {
			return maxOccupancy;
		}

		public void setMaxOccupancy(Integer maxOccupancy) {
			this.maxOccupancy = maxOccupancy;
		}

		public BigDecimal getPricePerNight() {
			return pricePerNight;
		}

		public void setPricePerNight(BigDecimal pricePerNight) {
			this.pricePerNight = pricePerNight;
		}

		public RoomType() {
		
		}

		public RoomType(Integer roomTypeId, String typeName, String description, Integer maxOccupancy,
				BigDecimal pricePerNight) {
			super();
			this.roomTypeId = roomTypeId;
			this.typeName = typeName;
			this.description = description;
			this.maxOccupancy = maxOccupancy;
			this.pricePerNight = pricePerNight;
		}

	
	    

}
