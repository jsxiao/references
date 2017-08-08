package com.px.core.http;

/**
 * 
 * @author Jason
 *
 */
public class HttpRequestMethodNotSupportedException extends RuntimeException{

	private static final long serialVersionUID = 3018472824305477555L;

	public HttpRequestMethodNotSupportedException() {
        super();
    }

    public HttpRequestMethodNotSupportedException(String message) {
        super(message);
    }

    public HttpRequestMethodNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestMethodNotSupportedException(Throwable cause) {
        super(cause);
    }

    protected HttpRequestMethodNotSupportedException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    @Override
    public Throwable fillInStackTrace() {
    	// TODO Auto-generated method stub
    	return super.fillInStackTrace();
    }
}
