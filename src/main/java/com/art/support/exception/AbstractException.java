package com.art.support.exception;

public abstract class AbstractException extends RuntimeException{

    /** 异常代码 */
    protected String retCode;

    /** 异常信息 */
    protected String retMessage;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }
}
