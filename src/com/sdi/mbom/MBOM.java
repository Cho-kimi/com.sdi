/**
 * 
 */
package com.sdi.mbom;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponent;

/**
 * @author cspark
 *
 */
public interface MBOM {
	
	public MBOMLine    getTopBOMLine();
	
	public List<MBOMLine> getChildBOMLine(MBOMLine parent);

	public int getChildrenCount();

	public TCComponent getSourceComponent(); 
	
}
