/**
 * 
 */
package com.sdi.mbom;

import com.sdi.mbom.impl.MBOMBuilderImpl;

/**
 * @author cspark
 *
 */
public class MBOMManager {
	
	
	private static  MBOMManager _instance;
	
	private static MBOMManager getInstance() {
		
		if(_instance == null) {
			_instance = new MBOMManager();
		}
		return _instance;
	}
	 
	public static void resetManager() {
		_instance = null;
	}

	public static MBOMBuilder getBuilder() {		
		
		return getInstance()._getBuilder();
	}
	
	protected MBOMBuilder _getBuilder() {
	
		return new MBOMBuilderImpl(_instance);
		
	}

}
