package com.zhilibao.service;

public class ResourceExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5938178916897833968L;

	public ResourceExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public ResourceExistsException(String message, Throwable cause,
//			boolean enableSuppression, boolean writableStackTrace) {
//		//super();
//		super(message, cause, enableSuppression, writableStackTrace);
//		// TODO Auto-generated constructor stub
//	}

	public ResourceExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
