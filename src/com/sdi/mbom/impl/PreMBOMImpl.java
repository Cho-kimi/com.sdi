package com.sdi.mbom.impl;

import java.util.List;

import com.sdi.mbom.MBOMBuilder;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.PreMBOM;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItemRevision;

public class PreMBOMImpl implements PreMBOM {
	
	private TCComponent sourceComponent;
	private String topName;
	
	private MBOMLine topBOMLine;
	private List<MBOMLine> childLines;

	private MBOMBuilder owningBuilder;
	
	public PreMBOMImpl(MBOMBuilder owningBuilder, String topMBOMLineName, TCComponentBOMLine bomline, boolean b) {
		this.owningBuilder = owningBuilder;
		this.topName = topMBOMLineName;
		this.sourceComponent = bomline;
		
		this.topBOMLine = owningBuilder.generateMBOMLine(topMBOMLineName, bomline);
	}

	@Override
	public MBOMLine getTopBOMLine() {
		return topBOMLine;
	}

	@Override
	public List<MBOMLine> getChildBOMLine(MBOMLine parent) {
		return this.childLines;
	}

	@Override
	public int getChildrenCount() {
		
		if(this.childLines == null) {
			return 0;			
		}
		
		return this.childLines.size();
	}

	@Override
	public MBOMBuilder getOwningBuilder() {
		return this.owningBuilder;
	}

	@Override
	public TCComponent getSourceComponent() {
		return this.sourceComponent;
	}

}
