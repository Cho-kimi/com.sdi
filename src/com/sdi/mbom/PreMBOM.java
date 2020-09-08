package com.sdi.mbom;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponentBOMWindow;

public interface PreMBOM extends MBOM {
	
	
	public MBOMBuilder getOwningBuilder();

	public List<MBOMLine> getPhantomMBOMLines();

	public void setBOMWindow(TCComponentBOMWindow bomWindow);

	/**
	 * @return the bomWindow
	 */
	TCComponentBOMWindow getBOMWindow();
	
}
