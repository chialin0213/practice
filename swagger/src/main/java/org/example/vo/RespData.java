package org.example.vo;

import lombok.Data;
import org.example.constant.ReturnCode;

@Data
public class RespData<T> {
    private long systemTime ;
    private int statusCode ;
    private String message ;
    private T data ;
    private boolean success ;

    public RespData(){
        this.systemTime = System.currentTimeMillis();
        this.success = true ;
    }

    public static <T> RespData<T> success(T data){
        RespData<T> respData = new RespData<>();
        respData.setStatusCode(ReturnCode.SUCCESS.getCode());
        respData.setMessage(ReturnCode.SUCCESS.getMessage());
        respData.setData(data);
        return respData ;
    }

    public static <T> RespData<T> fail(int code, String message){
        RespData<T> respData = new RespData<>();
        respData.setStatusCode(code);
        respData.setMessage(message);
        respData.setSuccess(false);
        return respData ;
    }
}
