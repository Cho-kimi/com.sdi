package com.sdi.mbom;

import java.awt.event.ActionListener;
import java.util.List;

import com.teamcenter.rac.kernel.TCComponentBOMLine;

/**
 * @Copyright : KIMIES
 * @author   : Á¶¼ºÇö
 * @since    : 2020. 08. 30
 * 
 * Package ID : com.sdi.mbom.MBOMLine.java
 * Type : Interface
 * 
 */
public interface MBOMLine extends MBOMComponent {

	public static final String NEW_ITEM_ID = "NEED NEW ITEM ID";
	
	public MBOMComponent getBOMComponent();
	
	public TCComponentBOMLine getPermanentBOMLine();

	public void setPermanentBOMLine(TCComponentBOMLine bomLine);
	
	public int getMBOMLineLevel();

	public MBOMLine getParent();

	public void setParent(MBOMLine parent);
	
	public MBOMLine[] getChildren();

	public List<MBOMLine> getChildrenList();

	public void addChildBOMLines(List<MBOMLine> mbomLines);

	public void addChildBOMLine(MBOMLine generateMBOMLine);

	public void removeChildBOMLine(MBOMLine childMBOMLine);
	
	public TCComponentBOMLine getSourceBOMLine();

	public String[] getRefPropertyNames();

	public String getTargetItemId();

	public void setTargetItemId(String targetItemId);
	
	public int getChildrenCount();
	
	public void setMBOMChangeEventHandler(MBOMChangeEventHandler handler);
	
	public ActionListener getDataChangeActionListener(PropertyUIProvider propertyProvider, String properyName);

	public PropertyUIProvider getPropertyUIProvider(String propertyName);

	
}
