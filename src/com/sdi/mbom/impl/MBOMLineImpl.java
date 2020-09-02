package com.sdi.mbom.impl;

import java.util.ArrayList;
import java.util.List;

import com.sdi.mbom.MBOMComponent;
import com.sdi.mbom.MBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;

public class MBOMLineImpl implements MBOMLine {
	
	
	
	private List<MBOMLine> childrenList;

	private boolean isPermanent;
	private String targetItemId;
	
	private TCComponentItem permanentItem;
	private TCComponentBOMLine permanentBOMLine;
	private TCComponentBOMLine sourceBOMLine;

	private String[] refPropertyNames;


	MBOMLineImpl(String permanatItemId, TCComponentBOMLine permanentBOMLine,  String[] refPropertyNames){
		if(permanentBOMLine == null) {
			throw new NullPointerException("주어진 BOMLine 정보가 없습니다.");
		}
		
		this.setTargetItemId(permanatItemId);
		this.permanentBOMLine = permanentBOMLine;		
		this.setRefPropertyNames(refPropertyNames);
		
		this.isPermanent = true;
	}
	
	MBOMLineImpl(TCComponentBOMLine sourceBOMLine, String targetItemId,  String[] refPropertyNames){
		
		this.setTargetItemId(targetItemId);
		this.setSourceBOMLine(sourceBOMLine);
		this.setRefPropertyNames(refPropertyNames);
		
		this.isPermanent = false;
	}
	
	

	@Override
	public boolean isPermanent() {
		return this.isPermanent;
	}

	@Override
	public TCComponentItem getPermanentItem() {
		return this.permanentItem;
	}

	@Override
	public TCComponentItemRevision getPermanentItemRevision() {
		return null;
	}

	@Override
	public MBOMComponent getBOMComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public TCComponentBOMLine getPermanentBOMLine() {
		return this.permanentBOMLine;
	}

	@Override
	public void addChildBOMLines(List<MBOMLine> mbomLines) {
		if(mbomLines  != null)
		getChildrenBOMLine().addAll(mbomLines);
	}

	@Override
	public void addChildBOMLine(MBOMLine generateMBOMLine) {
		if(generateMBOMLine != null)
		getChildrenBOMLine().add(generateMBOMLine);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<MBOMLine> getChildrenBOMLine(){
		if(this.childrenList == null) {
			this.childrenList =  new ArrayList<MBOMLine>();
		}
		return this.childrenList;
	}
	
	
	public void setChildrenBOMLine(List<MBOMLine> childrenList) {
		this.childrenList = childrenList;
	}

	@Override
	public String getTargetItemId() {
		return targetItemId;
	}

	@Override
	public void setTargetItemId(String targetItemId) {
		this.targetItemId = targetItemId;
	}

	@Override
	public TCComponentBOMLine getSourceBOMLine() {
		return sourceBOMLine;
	}

	public void setSourceBOMLine(TCComponentBOMLine sourceBOMLine) {
		this.sourceBOMLine = sourceBOMLine;
	}

	@Override
	public String[] getRefPropertyNames() {
		return refPropertyNames;
	}

	public void setRefPropertyNames(String[] refPropertyNames) {
		this.refPropertyNames = refPropertyNames;
	}

	@Override
	public int getChildrenCount() {
		return this.getChildrenBOMLine().size();
	}
	

}
