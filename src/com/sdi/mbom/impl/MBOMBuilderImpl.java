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
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_ITEM_TYPE", "MBOM 생성시 Item 유형은 지원하지 않습니다. BOMLine을 선택하여 주십시요"));
				}else if(targetComp  instanceof TCComponentItemRevision){
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_ITEM_REVISION_TYPE","MBOM 생성시 Item Revision 유형은 지원하지 않습니다. BOMLine을 선택하여 주십시요"));
				}else {
					throw new NotSupportedTypeException(registry.getString("MESSAGE_MBOM_NOT_SUPPORTED_DATA_TYPE","MBOM 생성을 지원하지 않는 유형이 선택되었습니다. BOMLine을 선택하여 주십시요"));
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
						
						//생산 구분자가 있는 경우에는 Phantom BOMLine을 생성하여 하위에 붙이기 위해 Phantom BOMLine을 가져온다.(최초에는 생성)
						if(address != null && address.length() > 0) {
							MBOMLine phantomMBOMLine = findPhantomChildBOMLine(phantomMBOMLines, address, relatedTopItemId, preMBOM);
							phantomMBOMLine.addChildBOMLine(childMBOMLine);
							
						}else {
							topline.addChildBOMLine(childMBOMLine);
						}
					}
					
					//추가된 phantomMBOMLine을 top에 등록한다.  
					//Phantom을 제일 나중에 추가하기 위하여 제일 나중에 추가한다.
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
		
		//찾지 못했으면 MBOMLine을 생성하고 리스트에 추가한다.
		TitledMBOMLine phantomMBOMLine = getHelper().generateTitledMBOMLine(phantomTitle, phantomLineName);
		phantomMBOMLine.setMBOMChangeEventHandler(handler);

		//index를 맞춰야 할경우 어떻게 가져올지는 고민해야할 문제 (Sorting의 기준 문제)
		phantomMBOMLines.add(phantomMBOMLine);
		
		return phantomMBOMLine;
	}
	

	@Override
	public void updateBOM(MBOM mbom) {
		// TODO Auto-generated method stub
		//MBOM을 구성하고 이전의 MBOM 구성을 제거한후 다시 구성한다.
		//이미 생성된 MBOM이 있을 경우 업데이트 한다.
		
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

		//화면에 임시로 만들어진 MBOM을 사용자가 만들기 명령을 클릭하면 실제로 아이템을 생성하고 BOM을 구성한다.
		// 이미 만드어진 아이템이 아닐 경우 아이템을 새로 생성한다.
		// 아이템이 만들어진 경우 새로 BOMLine만 생성하고 속성만 업데이트 한다.
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
