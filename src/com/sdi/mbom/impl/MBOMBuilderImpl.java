/**
 * 
 */
package com.sdi.mbom.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sdi.mbom.MBOM;
import com.sdi.mbom.MBOMBuilder;
import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventHandler;
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

/**
 * @author cspark
 *
 */
public class MBOMBuilderImpl implements MBOMBuilder {
	
	
	private static String [] propNames = new String[] {"Reference Degignator"};

	public MBOMBuilderImpl(MBOMManager _instance) {
		// TODO Auto-generated constructor stub
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
					// TODO targetComp���κ��� ���� Release�� ���������� ���� BOMWindow�� �޴´�.
					// TODO  Release�� �������� ���� ��쿡�� �ֽ� Working Revison�� �޴´�.	
					
					// TODO BOM Line�� �����Ͽ� �޴´�
					// TODO topBOMLine = bomWindow.getTOP();
					throw new NotSupportedTypeException("MBOM ������ Item ������ �������� �ʽ��ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�");
				}else if(targetComp  instanceof TCComponentItemRevision){
					// TODO targetComp���κ��� �����۸���������  BOMWindow�� �޴´�.
					// TODO BOM Line�� �����Ͽ� �޴´�
					// TODO topBOMLine = bomWindow.getTOP();
					throw new NotSupportedTypeException("MBOM ������ Item Revision ������ �������� �ʽ��ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�");
				}else {
					throw new NotSupportedTypeException("MBOM ������ �������� �ʴ� ������ ���õǾ����ϴ�. BOMLine�� �����Ͽ� �ֽʽÿ�");
				}
				
				if(topBOMLine != null && topBOMLine.getChildrenCount() > 0) {
					
					//Pack�� �Ǿ� ������ BOMLine �Ӽ��� ����� ������ �� �����Ƿ� Unpack�Ѵ�.
					if(topBOMLine.isPacked()) {
						topBOMLine.unpack();
					}
					
					String topMBOMLineName  = topBOMLine.getProperty("bl_item_object_name");  
					String relatedTopItemId = topBOMLine.getProperty("bl_item_item_id");  
					
					preMBOM = new PreMBOMImpl(this, topMBOMLineName, topBOMLine, false);
					
					List<MBOMLine> phantomMBOMLines = preMBOM.getPhantomMBOMLines();
					
					MBOMLine topline = preMBOM.getTopBOMLine();
					topline.setTargetItemId(MBOMLine.NEW_ITEM_ID);
					
					AIFComponentContext[] childrenLines = topBOMLine.getChildren();
					for(int i=0; i < childrenLines.length; i++ ) {
						TCComponentBOMLine childLine = (TCComponentBOMLine)childrenLines[i].getComponent();
						
						MBOMLine childMBOMLine = generateMBOMLine(childLine, false);
						childMBOMLine.setMBOMChangeEventHandler(preMBOM);
						
						String address = childLine.getStringProperty("m2_MbomAddress");
						//String address = childLine.getStringProperty("bl_usage_address");
						
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
		
		String phantomLineName = generateMBOMItemName(phantomTitle, lineSuffix);
		
		for(MBOMLine phantomMBOMLine : phantomMBOMLines) {
			if(phantomMBOMLine.getName().equals(phantomLineName)) {
				return phantomMBOMLine;
			}
		}
		
		//ã�� �������� MBOMLine�� �����ϰ� ����Ʈ�� �߰��Ѵ�.
		TitledMBOMLine phantomMBOMLine = generateTitledMBOMLine(phantomTitle, phantomLineName);
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
	public String generateMBOMItemName(String newName, String suffix) {
		if(suffix == null) return newName;
		return String.join("-", newName, suffix);
	}
	
	public TitledMBOMLine generateTitledMBOMLine(String title, String newObjectName) {		
		return new TitledMBOMLineImpl(title, newObjectName);
	}
	
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine permanent) {	
		return new MBOMLineImpl(permanent);
	}
		
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine bomline, boolean isPermanent ) {		
		return generateMBOMLine(bomline, null, null, propNames);
	}
	

	@Override
	public MBOMLine generateMBOMLine(String newObjectName) {		
		return new MBOMLineImpl(newObjectName);
	}

	@Override
	public MBOMLine generateMBOMLine(String newObjectName, String newItemId) {		
		return new MBOMLineImpl(newObjectName, newItemId);
	}

	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newItemId) {
		return generateMBOMLine(sourceBomline, null, newItemId, propNames);
	}

	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newObjectName, String newItemId, String[] refPropertyNames) {
		return new MBOMLineImpl(sourceBomline, false, newObjectName, newItemId, refPropertyNames);
	}


}
