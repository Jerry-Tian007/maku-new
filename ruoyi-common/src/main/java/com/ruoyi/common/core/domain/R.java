package com.ruoyi.common.core.domain;

import java.io.Serializable;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.i18n.ReturnMessageEnum;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    public static final int WARN = HttpStatus.WARN;

    /**
     * 失败
     */
    public static final int FAIL = HttpStatus.ERROR;

    private int code = 200;

    private String msg;

    private T data;

    public static <T> R<T> success() {
        return restResult(null, SUCCESS, ReturnMessageEnum.RETURN_SUCCESS.toString());
    }

    public static <T> R<T> success(T data) {
        return restResult(data, SUCCESS, ReturnMessageEnum.RETURN_SUCCESS.toString());
    }

    public static <T> R<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> error() {
        return restResult(null, FAIL, ReturnMessageEnum.RETURN_FAILED.toString());
    }

    public static <T> R<T> error(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> error(T data) {
        return restResult(data, FAIL, ReturnMessageEnum.RETURN_FAILED.toString());
    }

    public static <T> R<T> error(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }


    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg) {
        return restResult(null, WARN, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
