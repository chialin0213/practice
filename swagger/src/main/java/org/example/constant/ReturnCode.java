package org.example.constant;

public enum ReturnCode {
    SUCCESS(0, "成功"),FAIL(999, "失敗") ;

    private final int code ;
    private final String message ;

    ReturnCode(int code, String message){
        this.code = code ;
        this.message = message ;
    }

    public int getCode(){
        return code ;
    }

    public String getMessage(){
        return message ;
    }
}
