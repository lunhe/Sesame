package com.helun.menu.performance;

import com.helun.menu.model.BaseEntity;

public class LXXResult extends BaseEntity{
	
	public final static String FAIL_MSG = "服务器异常，请稍后重试";
	
	private int status;
	
	private String msg;
	
	private Object result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
