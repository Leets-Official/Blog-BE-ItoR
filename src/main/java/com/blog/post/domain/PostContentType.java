package com.blog.post.domain;

public enum PostContentType {
    TEXT("text"),
    IMAGE("image");

    private final String value;

    PostContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PostContentType from(String value) {
        for (PostContentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 content_type: " + value);
    }
}
