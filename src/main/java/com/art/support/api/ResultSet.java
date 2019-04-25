package com.art.support.api;

import com.art.support.common.ReturnCode;
import com.art.utils.SpringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResultSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8687621076620314576L;
	
	/** 返回码 */
	private String retCode;
	
	/** 返回信息 */
	private String retMessage;
	
	/** 返回数据结果集 */
	private Map<String, Object> result = new HashMap<String, Object>();
	
	public ResultSet(){

	}
	
	
	public ResultSet(String retCode, String retMessage, Object... args) {
		super();
		this.retCode = retCode;
		this.retMessage = SpringUtils.getApiMessage(retMessage, args);
	}

	/**
	 * 未登录错误返回
	 * @author never
	 * @param retMessage
	 * @return
	 */
	public static ResultSet notLoginError(String retMessage, Object... args){

		return new ResultSet(ReturnCode.merchantNotLogin, retMessage, args);

	}

	/**
	 * 通用成功返回
	 * @author never
	 * @param retMessage
	 * @return
	 */
	public static ResultSet success(String retMessage, Object... args){
		
		return new ResultSet(ReturnCode.success, retMessage, args);
		
	}
	
	/**
	 * 通用错误返回
	 * @author never
	 * @param retMessage
	 * @return
	 */
	public static ResultSet error(String retMessage, Object... args){
		
		return new ResultSet(ReturnCode.error, retMessage, args);
		
	}
	

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


	public Map<String, Object> getResult() {
		if(this != null && this instanceof PageResultSet){
			PageResultSet pageResultSet = (PageResultSet)this;
			if(pageResultSet != null){
				long total = pageResultSet.getTotal();
				long totalPages = pageResultSet.getTotalPages();
				long pageNumber = pageResultSet.getPageNumber();
				long pageSize = pageResultSet.getPageSize();

				result.put("total", total);
				result.put("totalPages", totalPages);
				result.put("pageNumber", pageNumber);
				result.put("pageSize", pageSize);
			}
		}
		return result;
	}


	public void setResult(Map<String, Object> result) {
		this.result = result;
	}


	/**
	 * 设值
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value){
		this.getResult().put(key, value);
	}


}
