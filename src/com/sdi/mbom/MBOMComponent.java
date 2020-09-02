package com.sdi.mbom;

import com.teamcenter.rac.kernel.TCComponent;

public interface MBOMComponent {
	
	public boolean isPermanent();
	
	public TCComponent getTCComponent();
	
}
