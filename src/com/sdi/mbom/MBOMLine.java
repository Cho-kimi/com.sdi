package com.sdi.mbom;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponentBOMLine;

public interface MBOMLine extends MBOMComponent {

	
	public MBOMComponent getBOMComponent();
	
	public TCComponentBOMLine getPermanentBOMLine();



	public String getName();

	List<MBOMLine> getChildrenBOMLine();

	void addChildBOMLines(List<MBOMLine> mbomLines);

	void addChildBOMLine(MBOMLine generateMBOMLine);

	TCComponentBOMLine getSourceBOMLine();

	String[] getRefPropertyNames();

	String getTargetItemId();

	void setTargetItemId(String targetItemId);
	
	int getChildrenCount();
	
	
}
