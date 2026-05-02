package com.example.Frontend.Entities;


public class Room {

	private Integer roomId;
	private Integer roomNumber;
	private Boolean isAvailable;
	private RoomType roomType;
	private Hotel hotel;

	// Constructors
	public Room() {
	}

	// Getters and Setters
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Room(Integer roomId, Integer roomNumber, Boolean isAvailable, RoomType roomType, Hotel hotel) {
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.isAvailable = isAvailable;
		this.roomType = roomType;
		this.hotel = hotel;
	}
}