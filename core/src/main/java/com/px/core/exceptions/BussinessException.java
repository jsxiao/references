package com.px.core.exceptions;

/**
 * 
 * @author xiaojs
 * 2016年5月24日 上午10:32:43
 */
public class BussinessException extends RuntimeException{

	private static final long serialVersionUID = 3408895673346568219L;

	private String uri;
	private String title;
	
	public BussinessException(String title, String message, String uri) {
        super(message);
        this.title = title;
        this.uri = uri;
    } 
	
	public BussinessException(String message, String uri){
		super(message);
		this.uri = uri;
	}
	
	public BussinessException() {
        super();
    }

    public String getUri() {
		return uri;
	}

	public String getTitle() {
		return title;
	}

	public BussinessException(String message) {
        super(message);
    }

    public BussinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinessException(Throwable cause) {
        super(cause);
    }
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
