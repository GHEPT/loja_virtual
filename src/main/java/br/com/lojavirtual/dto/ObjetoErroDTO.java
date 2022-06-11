package br.com.lojavirtual.dto;

import java.io.Serializable;

public class ObjetoErroDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String error;
	
	private String errorCode;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
