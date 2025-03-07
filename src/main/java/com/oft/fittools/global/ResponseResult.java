package com.oft.fittools.global;

import lombok.Data;

@Data
public class ResponseResult<T> {
    private String message;
    private T data;

    public static <T> ResponseResult<T> success(){
        UserContextHolder.clearUser();
        ResponseResult responseResult = new ResponseResult();
        responseResult.message="success";
        return responseResult;
    }

    public static <T> ResponseResult<T> success(T data){
        UserContextHolder.clearUser();
        ResponseResult responseResult = new ResponseResult();
        responseResult.data=data;
        responseResult.message="success";
        return responseResult;
    }
    public static <T> ResponseResult<T> fail(String message){
        UserContextHolder.clearUser();
        ResponseResult responseResult = new ResponseResult();
        responseResult.message=message;
        return responseResult;
    }
}
