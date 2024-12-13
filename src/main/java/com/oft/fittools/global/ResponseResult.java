package com.oft.fittools.global;

import lombok.Data;

@Data
public class ResponseResult<T> {
    private String message;
    private T data;

    public static <T> ResponseResult<T> success(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.message="success";
        return responseResult;
    }

    public static <T> ResponseResult<T> success(T data){
        ResponseResult responseResult = new ResponseResult();
        responseResult.data=data;
        responseResult.message="success";
        return responseResult;
    }
    public static <T> ResponseResult<T> fail(String message){
        ResponseResult responseResult = new ResponseResult();
        responseResult.message=message;
        return responseResult;
    }
}
