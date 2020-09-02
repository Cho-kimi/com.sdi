/**
 * 
 */
package com.sdi.mbom;

import com.teamcenter.rac.kernel.TCComponent;

/**
 * @author cspark
 *
 */
public interface MBOM extends MBOMChangeEventHandler{
	
	public MBOMLine    getTopBOMLine();
	
	public TCComponent getSourceComponent();
	
	public MBOMChangeEventHandler getMBOMChangeEventHandler();
	
}
