package com.sdi.mbom;

import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;

public interface MBOMComponent {
	
	public boolean isPermanent();

	
	public TCComponentItem getPermanentItem();
	
	public TCComponentItemRevision getPermanentItemRevision();

}
