/**
 * 
 */
package com.sdi.mbom;

/**
 * @author cspark
 *
 */
public class NotSupportedTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2286455293826962360L;

	/**
	 * @param message
	 */
	public NotSupportedTypeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotSupportedTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotSupportedTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NotSupportedTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
