package com.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qtycc qtycc@outlook.com
 * @className R
 * @description 统一返回类型
 * @date 2025/03/24 20:05:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<E> {
    /**
     * 0 - 成功
     * 1 - 失败
     */
    private Integer errno;
    private String message;
    private E data;

    public static <T> R<T> ok() {
        return new R<T>(0, "Action successed!", null);
    }

    public static <T> R<T> ok(String message) {
        return new R<T>(0, message, null);
    }

    public static <T> R<T> ok(String message, T data) {
        return new R<T>(0, message, data);
    }

    public static <T> R<T> fail() {
        return new R<T>(1, "Action failed!", null);
    }

    public static <T> R<T> fail(String message) {
        return new R<T>(1, message, null);
    }

    public static <T> R<T> fail(String message, T data) {
        return new R<T>(1, message, data);
    }
}
