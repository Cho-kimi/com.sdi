/**
 * 
 */
package com.sdi.mbom.impl;

import java.util.List;

import com.sdi.mbom.MBOM;
import com.sdi.mbom.MBOMBuilder;
import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventHandler;
import com.sdi.mbom.MBOMConstants;
import com.sdi.mbom.MBOMHelper;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.MBOMManager;
import com.sdi.mbom.NotSupportedTypeException;
import com.sdi.mbom.PreMBOM;
import com.sdi.mbom.TitledMBOMLine;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.Registry;

/**
 * @author cspark
 *
 */
public class MBOMBuilderImpl implements MBOMBuilder {
	
	private static Registry registry = Registry.getRegistry(com.sdi.mbom.MBOM.class);	
	
	public MBOMBuilderImpl() {
		
	}

	@Override
	public PreMBOM buildPreMBOM(InterfaceAIFComponent targetComp) {
		
		PreMBOM preMBOM = null;
		
		if(targetComp != null) {
			
			TCComponentBOMLine topBOMLine = null;
			
			try {
			
				if(targetComp instanceof TCComponentBOMLine) {
					topBOMLine =(TCComponentBOMLine)targetComp;
					
				}else if(targetComp  instanceof TCComponentItem){
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_ITEM_TYPE", "MBOM ������ Item ������ �������� �ʽ��ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�"));
				}else if(targetComp  instanceof TCComponentItemRevision){
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_ITEM_REVISION_TYPE","MBOM ������ Item Revision ������ �������� �ʽ��ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�"));
				}else {
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_DATA_TYPE","MBOM ������ �������� �ʴ� ������ ���õǾ����ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�"));
				}
				
				if(topBOMLine != null && topBOMLine.getChildrenCount() > 0) {
					
					String topMBOMLineName  = topBOMLine.getProperty(MBOMConstants.PROP_BOMLINE_OBJECT_NAME);  
					String relatedTopItemId = topBOMLine.getProperty(MBOMConstants.PROP_BOMLINE_ITEM_ID);  
					
					preMBOM = new PreMBOMImpl(this, topMBOMLineName, topBOMLine, false);
					
					List<MBOMLine> phantomMBOMLines = preMBOM.getPhantomMBOMLines();
					
					MBOMLine topline = preMBOM.getTopBOMLine();
					topline.setTargetItemId(MBOMLine.NEW_ITEM_ID);
					
					AIFComponentContext[] childrenLines = topBOMLine.getChildren();
					for(int i=0; i < childrenLines.length; i++ ) {
						TCComponentBOMLine childLine = (TCComponentBOMLine)childrenLines[i].getComponent();
						
						MBOMLine childMBOMLine = getHelper().generateMBOMLine(childLine, false);
						childMBOMLine.setMBOMChangeEventHandler(preMBOM);
						
						String address = childLine.getStringProperty(MBOMConstants.PROP_BOMLINE_MBOM_ADDRESS);
						
						//���� �����ڰ� �ִ� ��쿡�� Phantom BOMLine�� �����Ͽ� ������ ���̱� ���� Phantom BOMLine�� �����´�.(���ʿ��� ����)
						if(address != null && address.length() > 0) {
							MBOMLine phantomMBOMLine = findPhantomChildBOMLine(phantomMBOMLines, address, relatedTopItemId, preMBOM);
							phantomMBOMLine.addChildBOMLine(childMBOMLine);
							
						}else {
							topline.addChildBOMLine(childMBOMLine);
						}
					}
					
					//�߰��� phantomMBOMLine�� top�� ����Ѵ�.  
					//Phantom�� ���� ���߿� �߰��ϱ� ���Ͽ� ���� ���߿� �߰��Ѵ�.
					topline.addChildBOMLines(phantomMBOMLines);
				}
			}catch(NotSupportedTypeException nse) {
				throw nse;			
			}catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
		//
		return preMBOM;
	}
	
	
	/**
	 * 
	 * @param phantomMBOMLines
	 * @param phantomLineName
	 * @param handler
	 * @return
	 */
	private MBOMLine findPhantomChildBOMLine(List<MBOMLine> phantomMBOMLines, String phantomTitle, String lineSuffix, MBOMChangeEventHandler handler) {
		
		String phantomLineName = getHelper().generateMBOMItemName(phantomTitle, lineSuffix);
		
		for(MBOMLine phantomMBOMLine : phantomMBOMLines) {
			if(phantomMBOMLine.getName().equals(phantomLineName)) {
				return phantomMBOMLine;
			}
		}
		
		//ã�� �������� MBOMLine�� �����ϰ� ����Ʈ�� �߰��Ѵ�.
		TitledMBOMLine phantomMBOMLine = getHelper().generateTitledMBOMLine(phantomTitle, phantomLineName);
		phantomMBOMLine.setMBOMChangeEventHandler(handler);

		//index�� ����� �Ұ�� ��� ���������� ����ؾ��� ���� (Sorting�� ���� ����)
		phantomMBOMLines.add(phantomMBOMLine);
		
		return phantomMBOMLine;
	}
	

	@Override
	public void updateBOM(MBOM mbom) {
		// TODO Auto-generated method stub
		//MBOM�� �����ϰ� ������ MBOM ������ �������� �ٽ� �����Ѵ�.
		//�̹� ������ MBOM�� ���� ��� ������Ʈ �Ѵ�.
		
		MBOMLine topLine = mbom.getTopBOMLine();
		if(topLine != null) {
			MBOMChangeEventHandler handler = mbom.getMBOMChangeEventHandler();
			if(handler != null) {
				handler.fireMBOMChangeEvent(new MBOMChangeEvent("UPDATED"));
			}
		}
	}

	@Override
	public void buildBOM(PreMBOM mbom) {
		// TODO Auto-generated method stub

		//ȭ�鿡 �ӽ÷� ������� MBOM�� ����ڰ� ����� ����� Ŭ���ϸ� ������ �������� �����ϰ� BOM�� �����Ѵ�.
		// �̹� ������� �������� �ƴ� ��� �������� ���� �����Ѵ�.
		// �������� ������� ��� ���� BOMLine�� �����ϰ� �Ӽ��� ������Ʈ �Ѵ�.
		MBOMLine topLine = mbom.getTopBOMLine();
		if(topLine != null) {
			MBOMChangeEventHandler handler = mbom.getMBOMChangeEventHandler();
			if(handler != null) {
				handler.fireMBOMChangeEvent(new MBOMChangeEvent("CREATED"));
			}
		}
	}

	@Override
	public MBOMHelper getHelper() {
		return MBOMManager.getHelper();
	}	
	



}
