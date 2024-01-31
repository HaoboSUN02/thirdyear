package com.warning.common;

import java.io.Serializable;

/**
 * LayUI规范json数据格式
 */
public class ResponseResult<T> implements Serializable {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 将全部数据的条数作为count传给前台（一共多少条）
     */
    private Long count;
    /**
     * 将分页后的数据返回（每页要显示的数据）
     */
    private T data;

    /**
     * layUI专用
     *
     * @param data
     */
    public ResponseResult(T data) {
        this.code = 0;
        this.msg = "";
        this.data = data;
    }

    public ResponseResult(Long count, T data) {
        this.code = 0;
        this.msg = "";
        this.count = count;
        this.data = data;
    }

    public ResponseResult(String msg, T data) {
        this.code = 200;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>(null);
    }

    public static <T> ResponseResult<T> success(String msg) {
        return new ResponseResult<T>(200, msg, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(data);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return new ResponseResult<T>(msg, data);
    }

    public static <T> ResponseResult<T> failed() {
        return new ResponseResult<T>(500, "操作失败", null);
    }

    public static <T> ResponseResult<T> failed(String msg) {
        return new ResponseResult<T>(500, msg, null);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> ResponseResult<T> validateFailed() {
        return new ResponseResult<T>(404, "参数检验失败", null);
    }

    public static <T> ResponseResult<T> validateFailed(String msg) {
        return new ResponseResult<T>(404, msg, null);
    }

}
