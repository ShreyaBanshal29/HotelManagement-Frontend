package com.example.Frontend.dtos;

import java.util.List;

import com.example.Frontend.Entities.Amenity;

public class AmenityResponse {
	private List<Amenity> content;
    private PageInfo page;

    public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public List<Amenity> getContent() {
        return content;
    }

    public void setContent(List<Amenity> content) {
        this.content = content;
    }
}
