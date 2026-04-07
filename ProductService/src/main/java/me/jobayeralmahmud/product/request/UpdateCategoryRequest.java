package me.jobayeralmahmud.product.request;

public record UpdateCategoryRequest(String name, String description, Long parentId) {
}