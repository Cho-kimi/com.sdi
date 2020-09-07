/**
 * 
 */
package com.sdi.mbom;

/**
 * @author cspark
 *
 */
public class InvalidMBOMCreationException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2383700352936467400L;
	private String invalideSourceName;
	
	
	/**
	 * @param message
	 */
	public InvalidMBOMCreationException(String sourceName, String message) {
		super(message);
		this.setInvalideSourceName(sourceName);
	}

	/**
	 * @param cause
	 */
	public InvalidMBOMCreationException(String sourceName, Throwable cause) {
		super(cause);
		this.setInvalideSourceName(sourceName);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidMBOMCreationException(String sourceName, String message, Throwable cause) {
		super(message, cause);
		this.setInvalideSourceName(sourceName);
	}

	public String getInvalideSourceName() {
		return invalideSourceName;
	}

	public void setInvalideSourceName(String invalideSourceName) {
		this.invalideSourceName = invalideSourceName;
	}
	
	

}
