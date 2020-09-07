package com.sdi.mbom.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.JTextComponent;

import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventHandler;
import com.sdi.mbom.MBOMComponent;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.PropertyUIProvider;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;

public class MBOMLineImpl implements MBOMLine {
	
	
	
	private List<MBOMLine> childrenList;

	private boolean isPermanent;
	private String targetItemId;
	
	private TCComponentBOMLine permanentBOMLine;
	private TCComponentBOMLine sourceBOMLine;

	private String[] refPropertyNames;

	private String objectName;
	
	private MBOMChangeEventHandler changeEventHandler;

	private ActionListener actionListener;

	private Map<String, PropertyUIProvider> propertyUIProviderMap;

	MBOMLineImpl(String newObjectName){
		this(newObjectName, NEW_ITEM_ID);
	}
	
	MBOMLineImpl(String newObjectName, String newItemId){
		this(null, false, newObjectName, newItemId, null);
	}
	
	MBOMLineImpl(TCComponentBOMLine permanentBOMLine){
		this(permanentBOMLine, true, null, null, null);
		
		if(permanentBOMLine == null) {
			throw new NullPointerException("주어진 BOMLine 정보가 없습니다.");
		}
	}

	MBOMLineImpl(TCComponentBOMLine sourceBOMLine, String newItemId,  String[] refPropertyNames){
		this(sourceBOMLine, false, null, newItemId, refPropertyNames );
	}
	
	MBOMLineImpl(TCComponentBOMLine bomline, boolean isPermanent, String newObjectName, String newItemId,  String[] refPropertyNames){
		
		this.childrenList =  new ArrayList<MBOMLine>();
		this.propertyUIProviderMap = new HashMap<String, PropertyUIProvider>();
		
		this.isPermanent = isPermanent;
		if(isPermanent) {
			this.permanentBOMLine = bomline;
		}else {
			
			this.sourceBOMLine = bomline;
			this.refPropertyNames = refPropertyNames;
			
			//아이템을 새로 생성해야 할 경우 반드시 newItemId를 전달하거나 source BOMLine이 null이어야 한다.
			//topline의 경우 source가 null이 아니므로 반드시 newItemId의 값이 전달되어야 한다.			
			if(newItemId != null || this.sourceBOMLine == null ) {
				this.targetItemId = (newItemId != null)? newItemId : NEW_ITEM_ID;
			}
			this.setObjectName(newObjectName);
		}
	}
	
	
	@Override
	public boolean isPermanent() {
		return this.isPermanent;
	}

	@Override
	public MBOMComponent getBOMComponent() {
		return this;
	}

	@Override
	public TCComponentBOMLine getPermanentBOMLine() {
		return this.permanentBOMLine;
	}

	@Override
	public void addChildBOMLines(List<MBOMLine> mbomLines) {
		if(mbomLines  != null)
		getChildrenList().addAll(mbomLines);
	}

	@Override
	public void addChildBOMLine(MBOMLine generateMBOMLine) {
		if(generateMBOMLine != null)
		getChildrenList().add(generateMBOMLine);
	}

	@Override
	public String getName() {
		return getObjectName();
	}
	
	public String getObjectName() {
		return this.objectName;
	}
	
	protected void setObjectName(String newObjectName) {

		try {
			//이미 생성된 BOMLine이 있으면 해당 BOMLine 정보로 설정
			if(this.permanentBOMLine != null){
				this.objectName = this.permanentBOMLine.getProperty("bl_item_object_name");
				
			//신규 생성할 BOMLine 이름이 주어지면 신규 이름으로 표시
			}else if(newObjectName != null && newObjectName.length() > 0) {
				this.objectName = newObjectName;
				
			//신규 이름이 NULL이고 Source가 있을 경우 Source의 이름을 가져옴
			}else if(sourceBOMLine != null ) {
				this.objectName = sourceBOMLine.getProperty("bl_item_object_name");
					
			}else {
				this.objectName = "NO_NAME";
			}
		} catch (Throwable e) {
			this.objectName = "NO_NAME";
			e.printStackTrace();
		}
	}
	
	@Override
	public void setMBOMChangeEventHandler(MBOMChangeEventHandler handler) {
		this.changeEventHandler = handler;
	}
	
	@Override
	public MBOMLine[] getChildren(){
		return this.childrenList.toArray(new MBOMLine[this.childrenList.size()]);
	}
	
	@Override
	public List<MBOMLine> getChildrenList(){
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
		this.changeEventHandler.fireMBOMChangeEvent(new MBOMChangeEvent(this));
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
		return this.getChildrenList().size();
	}

	@Override
	public List<Object> getProperties(String[] propNames) {
		
		TCComponentBOMLine bomLine = null;
		List<Object> propValues = new ArrayList<Object>();
		
		if(propNames == null || propNames.length <= 0) {
			return propValues ;
		}
		
		bomLine = (this.isPermanent)? this.getPermanentBOMLine(): this.getSourceBOMLine();
		
		for(String propName : propNames) {
			
			String propValue = "";
			try {
			if(propName.equals("object_name") || propName.equals("bl_item_object_name")) {
				propValue = getName();
			}else if(propName.equals("item_id") || propName.equals("bl_item_item_id")) {
				
				//생성할 대상 아이템 아이디를 받은 경우에는 소스가 있더라도 아이템을 생성해야하므로 대상 ID를 반환한다. top line이 대표적
				// Phantom의 경우에는 타겟이 없더라도 source bomline이 없다.
				if(getTargetItemId() != null) {
					propValue = getTargetItemId();
				}else {
					propValue = bomLine == null? "" : bomLine.getItem().getStringProperty("item_id");
				}
			}else {
				propValue = bomLine.getProperty(propName);
			}
			}catch(Throwable t) {
				t.printStackTrace();
			}
			propValues.add(propValue);
		}
		
		return propValues;
	}

	@Override
	public TCComponent getTCComponent() {
		return (this.isPermanent)?this.permanentBOMLine : this.sourceBOMLine;
	}

	@Override
	public ActionListener getDataChangeActionListener(PropertyUIProvider propertyProvider, final String propertyName) {
		
		if(this.actionListener == null) {
			this.actionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					Object source = e.getSource();
					String value = null;
					if(source instanceof JTextComponent) {
						value= ((JTextComponent)source).getText();
					}else {
						value = source.toString();
					}
					propetyUpdate(propertyName, value);
				}
				
			};
		}
		
		addPropertyProvider(propertyName, propertyProvider);
		
		return this.actionListener;
	}
	
	
	protected void addPropertyProvider(String propertyName, PropertyUIProvider propertyProvider) {
		if(!this.propertyUIProviderMap.containsKey(propertyName) || this.propertyUIProviderMap.get(propertyName) != propertyProvider) {
			this.propertyUIProviderMap.put(propertyName, propertyProvider );
		}
	}

	private void propetyUpdate(String propertyName, String value ) {
		
		if("item_id".equals(propertyName)) {
			this.setTargetItemId(value);			
		}else if("object_name".equals(propertyName)) {
			this.setObjectName(value);
		}
		
	}
	

}
