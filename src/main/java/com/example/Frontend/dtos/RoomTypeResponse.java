package com.example.Frontend.dtos;

import java.util.List;
import com.example.Frontend.Entities.RoomType;

public class RoomTypeResponse {

    private List<RoomType> content;
    private PageInfo page;

    public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public List<RoomType> getContent() {
        return content;
    }

    public void setContent(List<RoomType> content) {
        this.content = content;
    }
}