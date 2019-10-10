package com.helun.menu.exception;

public class MenuRunTimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msgCn;
	private String msgEn;
	private Integer errCode;

	public MenuRunTimeException() {

	}

	public MenuRunTimeException(Integer errCode) {

		this.errCode = errCode;
	}

	public MenuRunTimeException(String msgEn) {

		this.msgEn = msgEn;
	}

	public MenuRunTimeException(String msgCn, Integer errCode) {

		this.errCode = errCode;
		this.msgCn = msgCn;
	}

	public MenuRunTimeException(String msgCn, String msgEn, Integer errCode) {

		this.errCode = errCode;
		this.msgCn = msgCn;
		this.msgEn = msgEn;
	}

	public MenuRunTimeException(Exception e) {
		super(e);
	}

	public MenuRunTimeException(Integer errCode, Exception e) {
		super(e);
		this.errCode = errCode;
	}

	public MenuRunTimeException(String msgEn, Exception e) {
		super(e);
		this.msgEn = msgEn;
	}

	public MenuRunTimeException(String msgCn, Integer errCode, Exception e) {
		super(e);
		this.errCode = errCode;
		this.msgCn = msgCn;
	}

	public MenuRunTimeException(String msgCn, String msgEn, Integer errCode, Exception e) {
		super(e);
		this.errCode = errCode;
		this.msgCn = msgCn;
		this.msgEn = msgEn;
	}

	public String getMsg() {
		return msgEn != null ? msgEn : msgCn;
	}

	public String getMsgCn() {
		return msgCn;
	}

	public MenuRunTimeException setMsgCn(String msgCn) {
		this.msgCn = msgCn;
		return this;
	}

	public String getMsgEn() {
		return msgEn;
	}

	public MenuRunTimeException setMsgEn(String msgEn) {
		this.msgEn = msgEn;
		return this;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public MenuRunTimeException setErrCode(Integer errCode) {
		this.errCode = errCode;
		return this;
	}

}
