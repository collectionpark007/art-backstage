package com.art.support.exception;

import com.art.support.common.ReturnCode;
import com.art.utils.SpringUtils;

public class ApiException extends AbstractException{


    public ApiException(String retCode, String retMessage) {
        super();
        this.retCode = retCode;
        this.retMessage = SpringUtils.getApiMessage(retMessage);
    }

    public ApiException(String retMessage) {
        super();
        this.retCode = ReturnCode.error;
        this.retMessage = SpringUtils.getApiMessage(retMessage);
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append(getClass());
        sb.append(" api接口异常： 【错误码：");
        sb.append(retCode);
        sb.append("】, 【错误信息：");
        sb.append(retMessage);
        sb.append("】");
        return sb.toString();
    }
}
