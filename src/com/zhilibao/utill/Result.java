package com.zhilibao.utill;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Result {
	private boolean successful = true;
	
	private String msg;
	
	private Object data;
	
	public Result() {
	}
	
	public Result(boolean successful) {
		this(successful, null);
	}

	public Result(String msg) {
		this(false, msg);
	}
	
	public Result(boolean successful, String msg) {
		if(StringUtils.isBlank(msg)){
			msg = "操作失败,请联系管理员。";
		}
		this.successful = successful;
		this.msg = msg;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * 只返回code和message
	 * @param code
	 * @param message
	 * @return
	 */
	public static Map<String,String> getResult(String code,String message){
		Map<String,String> map = new HashMap<String, String>(1);
		map.put("code", code);
		map.put("message", message);
		return map;
	}
	/**
	 * 返回code,message和data
	 * @param code
	 * @param message
	 * @param data
	 * @return
	 */
	public static Map<String,Object> getResult(String code,String message,Map<String,Object> data){
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("code", code);
		map.put("data", data);
		map.put("message", message);
		return map;
	}
}
