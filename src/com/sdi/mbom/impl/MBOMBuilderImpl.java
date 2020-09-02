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
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.MBOMManager;
import com.sdi.mbom.PreMBOM;
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
				// TODO targetComp으로부터 최종 Release된 아이템으로 부터 BOMWindow를 받는다.
				// TODO  Release된 아이템이 없을 경우에는 최신 Working Revison을 받는다.	
				
				// TODO BOM Line을 생성하여 받는다
				// TODO topBOMLine = bomWindow.getTOP();
			}else if(targetComp  instanceof TCComponentItemRevision){
				// TODO targetComp으로부터 아이템리비전으로  BOMWindow를 받는다.
				// TODO BOM Line을 생성하여 받는다
				// TODO topBOMLine = bomWindow.getTOP();
			}
			

			
			if(topBOMLine != null && topBOMLine.getChildrenCount() > 0) {
				
				String topMBOMLineName = topBOMLine.getItem().getStringProperty("object_name");  //속성명 확인필요
				String relatedTopItemId = topBOMLine.getItem().getStringProperty("item_id");  //속성명 확인필요
				
				preMBOM = new PreMBOMImpl(this, topMBOMLineName, topBOMLine, false);
				
				//List<MBOMLine> phantomMBOMLines = 
				Map<String,MBOMLine> phantomMBOMLineMap = new HashMap<String,MBOMLine>();
				
				//preMBOM.getTopBOMLine().addChildBOMLines(phantomMBOMLines);
				
				MBOMLine topline = preMBOM.getTopBOMLine();
				
				AIFComponentContext[] childrenLines = topBOMLine.getChildren();
				for(int i=0; i < childrenLines.length; i++ ) {
					TCComponentBOMLine childLine = (TCComponentBOMLine)childrenLines[i].getComponent();
					String childMBOMLineName = childLine.getItem().getStringProperty("object_name");  //속성명 확인필요
					
					MBOMLine childMBOMLine = generateMBOMLine(childMBOMLineName, childLine);
					
					String address = childLine.getStringProperty("m2_MbomAddress");
					
					if(address != null && address.length() > 0) {
						
						if(phantomMBOMLineMap.containsKey(address)) {
							phantomMBOMLineMap.get(address).addChildBOMLine(childMBOMLine);
						}else {
							MBOMLine phantomMBOMLine = generateMBOMLine(address + relatedTopItemId);
							phantomMBOMLineMap.put(address, phantomMBOMLine);
							phantomMBOMLine.addChildBOMLine(childMBOMLine);
							
							//topline.addChildBOMLine(phantomMBOMLine);
						}
					}else {
						topline.addChildBOMLine(childMBOMLine);
					}
					
					//address에 따라 새로운 Phantom을 만들고 그 밑에 BOMLine 아이템을 Child로 붙인다.
					
				}
				
				if(!phantomMBOMLineMap.isEmpty()) {
					for(MBOMLine phantomLine :  phantomMBOMLineMap.values()) {
						topline.addChildBOMLine(phantomLine);
					}
				}
				
			}
			
			}catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
		
		
		//
		return preMBOM;
	}
	@Override
	public MBOMLine generateMBOMLine(String name) {		
		return generateMBOMLine(name, null, null, propNames);
	}
	@Override
	public MBOMLine generateMBOMLine(String name, TCComponentBOMLine source) {		
		return generateMBOMLine(name, null, source, propNames);
	}
	@Override
	public MBOMLine generateMBOMLine(String name, TCComponentBOMLine permanent, TCComponentBOMLine source) {	
		return generateMBOMLine(name, permanent, source, propNames);
	}
	
	/**
	 * 
	 * @param name                새로이 만들어질 BOMLine의 이름
	 * @param comp                원래의 BOMLine 아이템
	 * @param refPropertyNames  , 원래의 BOMLine으로 부터 복사할 속성명 배열
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(String newObjectName, TCComponentBOMLine permanent, TCComponentBOMLine source, String[] refPropertyNames) {
		
		MBOMLine newMBOMLine = null;
		
		if(permanent == null) {
			newMBOMLine = new MBOMLineImpl(source, newObjectName, refPropertyNames);
		}else {
			newMBOMLine = new MBOMLineImpl(newObjectName, permanent, refPropertyNames);
		}
		
		return newMBOMLine;
	}
	

	@Override
	public void updateBOM(MBOM mbom) {
		// TODO Auto-generated method stub
		//MBOM을 구성하고 이전의 MBOM 구성을 제거한후 다시 구성한다.
		//이미 생성된 MBOM이 있을 경우 업데이트 한다.
	}

	@Override
	public void buildBOM(PreMBOM mbom) {
		// TODO Auto-generated method stub

		//화면에 임시로 만들어진 MBOM을 사용자가 만들기 명령을 클릭하면 실제로 아이템을 생성하고 BOM을 구성한다.
		// 이미 만드어진 아이템이 아닐 경우 아이템을 새로 생성한다.
		// 아이템이 만들어진 경우 새로 BOMLine만 생성하고 속성만 업데이트 한다.
	}

}
