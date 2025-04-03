package com.blog.common.response;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {

        return new ApiResponse<>(true, "요청이 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> error(String message) {

        return new ApiResponse<>(false, message, null);
    }

    // Getter 추가
    public boolean isSuccess() {
        return success; }
    public String getMessage() {
        return message; }
    public T getData() {
        return data; }
}
