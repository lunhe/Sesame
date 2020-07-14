package com.helun.menu.performance;

/**
 * User: Lib
 * Date: 2016/4/6.
 * Time: 17:23
 */
public class LXXRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LXXRuntimeException(){
        super();
    }

    public LXXRuntimeException(Exception e){
	    super(e);
    }

    public LXXRuntimeException(String exceptionMsg){
        super(exceptionMsg);
    }
    
    public LXXRuntimeException(String format, Object... args){
        super(String.format(format, args));
    }
}
