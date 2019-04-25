package com.art.support.exception;

import com.art.support.common.ReturnCode;
import com.art.utils.SpringUtils;

public class BusinessException extends AbstractException{


	public BusinessException(String retCode, String retMessage) {
		super();
		this.retCode = retCode;
		this.retMessage = SpringUtils.getApiMessage(retMessage);
	}

	public BusinessException(String retMessage) {
		super();
		this.retCode = ReturnCode.error;
		this.retMessage = SpringUtils.getApiMessage(retMessage);
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(getClass());
		sb.append(" 业务异常： 【错误码：");
		sb.append(retCode);
		sb.append("】, 【错误信息：");
		sb.append(retMessage);
		sb.append("】");
		return sb.toString();
	}

}
