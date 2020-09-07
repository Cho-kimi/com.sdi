package com.sdi.mbom;

import java.awt.event.ActionListener;
import java.util.List;

import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.util.iTextField;

public interface MBOMLine extends MBOMComponent {

	public static final String NEW_ITEM_ID = "NEED NEW ITEM ID";
	
	public MBOMComponent getBOMComponent();
	
	public TCComponentBOMLine getPermanentBOMLine();

	public String getName();
	
	MBOMLine[] getChildren();

	List<MBOMLine> getChildrenList();

	void addChildBOMLines(List<MBOMLine> mbomLines);

	void addChildBOMLine(MBOMLine generateMBOMLine);

	TCComponentBOMLine getSourceBOMLine();

	String[] getRefPropertyNames();

	String getTargetItemId();

	void setTargetItemId(String targetItemId);
	
	int getChildrenCount();
	
	List<Object> getProperties(String [] propNames);

	void setMBOMChangeEventHandler(MBOMChangeEventHandler handler);
	
	ActionListener getDataChangeActionListener(PropertyUIProvider propertyProvider, String properyName);
	
}
