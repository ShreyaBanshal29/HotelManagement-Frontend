package com.example.Frontend.dtos;

import java.util.List;

/**
 * Generic page view passed to Thymeleaf templates.
 * Mirrors the fields used in hotels.html pagination block.
 */
public class PageView<T> {
    private List<T> content;
    private int number;        // 0-based current page
    private int size;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;

    public PageView(List<T> content, PageInfo page) {
        this.content = content;
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.numberOfElements = content.size();
    }

    public List<T> getContent() { return content; }
    public int getNumber() { return number; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getNumberOfElements() { return numberOfElements; }
    public boolean isFirst() { return number == 0; }
    public boolean isLast() { return number >= totalPages - 1; }
}
