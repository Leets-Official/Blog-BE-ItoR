package com.blog.domain.post.domain;

public enum ContentType {
    TEXT("TEXT"),
    IMAGE("IMAGE");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
