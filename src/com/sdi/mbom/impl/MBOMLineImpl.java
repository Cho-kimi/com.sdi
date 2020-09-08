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
import com.sdi.mbom.MBOMConstants;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.PropertyUIProvider;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCException;

public class MBOMLineImpl implements MBOMLine {
	
	private List<MBOMLine> childrenList;

	private boolean isPermanent;
	private String targetItemId;
	
	private MBOMLine parent;
	
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
			throw new NullPointerException("�־��� BOMLine ������ �����ϴ�.");
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
			
			//�������� ���� �����ؾ� �� ��� �ݵ�� newItemId�� �����ϰų� source BOMLine�� null�̾�� �Ѵ�.
			//topline�� ��� source�� null�� �ƴϹǷ� �ݵ�� newItemId�� ���� ���޵Ǿ�� �Ѵ�.			
			if(newItemId != null || this.sourceBOMLine == null ) {
				this.targetItemId = (newItemId != null)? newItemId : NEW_ITEM_ID;
			}else {
				this.targetItemId = null;
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
	public void setPermanentBOMLine(TCComponentBOMLine bomLine) {
		this.permanentBOMLine = bomLine;
		this.setObjectName(null);
		this.isPermanent = true;
	}

	@Override
	public void addChildBOMLines(List<MBOMLine> mbomLines) {
		if(mbomLines  != null) {
			for(MBOMLine bomLine : mbomLines) {
				addChildBOMLine(bomLine);
			}
		}
	}

	@Override
	public void addChildBOMLine(MBOMLine childMBOMLine) {
		if(childMBOMLine != null && getChildrenList().indexOf(childMBOMLine) < 0 ) {
			getChildrenList().add(childMBOMLine);
			childMBOMLine.setParent(this);
		}
	}
	
	@Override
	public void removeChildBOMLine(MBOMLine childMBOMLine) {
		if(childMBOMLine != null) {
			if(getChildrenList().indexOf(childMBOMLine) != -1) {
				getChildrenList().remove(childMBOMLine);
			}
		}
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
			//�̹� ������ BOMLine�� ������ �ش� BOMLine ������ ����
			if(this.permanentBOMLine != null){
				this.objectName = this.permanentBOMLine.getProperty(MBOMConstants.PROP_BOMLINE_OBJECT_NAME);
				
			//�ű� ������ BOMLine �̸��� �־����� �ű� �̸����� ǥ��
			}else if(newObjectName != null && newObjectName.length() > 0) {
				this.objectName = newObjectName;
				
			//�ű� �̸��� NULL�̰� Source�� ���� ��� Source�� �̸��� ������
			}else if(sourceBOMLine != null ) {
				this.objectName = sourceBOMLine.getProperty(MBOMConstants.PROP_BOMLINE_OBJECT_NAME);
					
			}else {
				this.objectName = MBOMConstants.PROP_BLANK;
			}
		} catch (Throwable e) {
			this.objectName = MBOMConstants.PROP_BLANK;
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
	public String getProperty(String propName) {
		
		String propValue = MBOMConstants.PROP_BLANK;
		
		try {
			TCComponent comp = getTCComponent();
			
			if(propName.equals(MBOMConstants.PROP_OBJECT_NAME) || propName.equals(MBOMConstants.PROP_BOMLINE_OBJECT_NAME)) {
				propValue = getName();
			}else if(propName.equals(MBOMConstants.PROP_ITEM_ID) || propName.equals(MBOMConstants.PROP_BOMLINE_ITEM_ID)) {
				//������ ��� ������ ���̵� ���� ��쿡�� �ҽ��� �ִ��� �������� �����ؾ��ϹǷ� ��� ID�� ��ȯ�Ѵ�. top line�� ��ǥ��
				// Phantom�� ��쿡�� Ÿ���� ������ source bomline�� ����.
				if(!this.isPermanent && getTargetItemId() != null) {
					propValue = getTargetItemId();
				}else {
					propValue = comp == null? MBOMConstants.PROP_BLANK : getProperty(MBOMConstants.PROP_BOMLINE_ITEM_ID, comp, MBOMConstants.PROP_BLANK);
				}
				
			}else if(propName.equals(MBOMConstants.MBOM_LINE_LEVEL_HEADER)) {
				propValue = String.valueOf(getMBOMLineLevel());
			}else if(propName.equals(MBOMConstants.MBOM_LINE_STATUS_HEADER)) {
				propValue = getMBOMLineStatus();
			}else {
				propValue = getProperty(propName, comp, MBOMConstants.PROP_BLANK);
			}
		}catch(Throwable t) {
			t.printStackTrace();
		}

		return propValue;
	}
	
	protected String getProperty(String propName, TCComponent comp) throws TCException {
		return getProperty(propName, comp, null);
	}

	protected String getProperty(String propName, TCComponent comp, String nullValue) throws TCException {
		
		if(comp != null) {
			return comp.getStringProperty(propName);
		}
		
		return nullValue;
	}


	@Override
	public List<Object> getProperties(String[] propNames) {

		List<Object> propValues = new ArrayList<Object>();
		if(propNames != null) {
			for(String propName : propNames) {
				propValues.add(getProperty(propName));
			}
		}
		return propValues;
	}

	public String getMBOMLineStatus() {

		if(isPermanent() || this.getPermanentBOMLine() != null) {
			return MBOMConstants.MBOM_LINE_STATUS_COMPLETE;
		}else if(getTargetItemId() != null ){
			return MBOMConstants.MBOM_LINE_STATUS_NEW;
		}else if(getSourceBOMLine() != null ){
			return MBOMConstants.MBOM_LINE_STATUS_ADD;
		}
		return  MBOMConstants.PROP_BLANK;
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
	
	@Override
	public PropertyUIProvider getPropertyUIProvider(String propertyName) {
		if(this.propertyUIProviderMap.containsKey(propertyName)) {
			return this.propertyUIProviderMap.get(propertyName);
		}
		return null;
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

	@Override
	public int getMBOMLineLevel() {
		return (getParent() == null)? 0 : getParent().getMBOMLineLevel() + 1;
	}

	@Override
	public MBOMLine getParent() {
		return this.parent;
	}

	@Override
	public void setParent(MBOMLine parent) {
		this.parent = parent;
		
	}
	

}
