/**
 * 
 */
package com.sdi.mbom.impl;

import java.util.ArrayList;
import java.util.List;

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
				// TODO targetComp���κ��� ���� Release�� ���������� ���� BOMWindow�� �޴´�.
				// TODO  Release�� �������� ���� ��쿡�� �ֽ� Working Revison�� �޴´�.	
				
				// TODO BOM Line�� �����Ͽ� �޴´�
				// TODO topBOMLine = bomWindow.getTOP();
			}else if(targetComp  instanceof TCComponentItemRevision){
				// TODO targetComp���κ��� �����۸���������  BOMWindow�� �޴´�.
				// TODO BOM Line�� �����Ͽ� �޴´�
				// TODO topBOMLine = bomWindow.getTOP();
			}
			

			
			if(topBOMLine != null && topBOMLine.getChildrenCount() > 0) {
				
				String topMBOMLineName = topBOMLine.getItem().getStringProperty("object_name");  //�Ӽ��� Ȯ���ʿ�
				String relatedTopItemId = topBOMLine.getItem().getStringProperty("item_id");  //�Ӽ��� Ȯ���ʿ�
				
				preMBOM = new PreMBOMImpl(this, topMBOMLineName, topBOMLine, false);
				
				List<MBOMLine> phantomMBOMLines = makePhantomMBOMLines(relatedTopItemId);
				
				preMBOM.getTopBOMLine().addChildBOMLines(phantomMBOMLines);
				
				
				AIFComponentContext[] childrenLines = topBOMLine.getChildren();
				for(int i=0; i < childrenLines.length; i++ ) {
					TCComponentBOMLine childLine = (TCComponentBOMLine)childrenLines[i].getComponent();
					
					String address = childLine.getStringProperty("MBOMAddress");
					
					for( MBOMLine parentLine : phantomMBOMLines) {
						if( parentLine.getName().startsWith(address)) {
							parentLine.addChildBOMLine(generateMBOMLine("", childLine));
						}						
					}
					
					//address�� ���� ���ο� Phantom�� ����� �� �ؿ� BOMLine �������� Child�� ���δ�.
					
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

	private List<MBOMLine> makePhantomMBOMLines(String relatedTopItemId) {
		// TODO Auto-generated method stub
		//�Ӽ� ������ MBOMLine�� �����.
		List<MBOMLine> bomLines = new ArrayList<MBOMLine>();
		
		//3�� �Ӽ��� ������ For���� ���鼭 �����Ѵ�.
		String phantomName = "AUTO";
		
		MBOMLine line = generateMBOMLine(phantomName + relatedTopItemId);
		bomLines.add(line);	
		
		return bomLines;
	}
	
	public MBOMLine generateMBOMLine(String name) {		
		return generateMBOMLine(name, null, null, propNames);
	}
	
	public MBOMLine generateMBOMLine(String name, TCComponentBOMLine source) {		
		return generateMBOMLine(name, null, source, propNames);
	}
	
	public MBOMLine generateMBOMLine(String name, TCComponentBOMLine target, TCComponentBOMLine source) {	
		return generateMBOMLine(name, target, source, propNames);
	}
	
	/**
	 * 
	 * @param name                ������ ������� BOMLine�� �̸�
	 * @param comp                ������ BOMLine ������
	 * @param refPropertyNames  , ������ BOMLine���� ���� ������ �Ӽ��� �迭
	 * @return
	 */
	public MBOMLine generateMBOMLine(String targetItemId, TCComponentBOMLine target, TCComponentBOMLine source, String[] refPropertyNames) {
		
		MBOMLine newMBOMLine = null;
		
		if(target == null) {
			newMBOMLine = new MBOMLineImpl(source, targetItemId, refPropertyNames);
		}else {
			newMBOMLine = new MBOMLineImpl(targetItemId, target, refPropertyNames);
		}
		
		return newMBOMLine;
	}
	

	@Override
	public void updateBOM(MBOM mbom) {
		// TODO Auto-generated method stub
		//MBOM�� �����ϰ� ������ MBOM ������ �������� �ٽ� �����Ѵ�.
		//�̹� ������ MBOM�� ���� ��� ������Ʈ �Ѵ�.
	}

	@Override
	public void buildBOM(PreMBOM mbom) {
		// TODO Auto-generated method stub

		//ȭ�鿡 �ӽ÷� ������� MBOM�� ����ڰ� ����� ����� Ŭ���ϸ� ������ �������� �����ϰ� BOM�� �����Ѵ�.
		// �̹� ������� �������� �ƴ� ��� �������� ���� �����Ѵ�.
		// �������� ������� ��� ���� BOMLine�� �����ϰ� �Ӽ��� ������Ʈ �Ѵ�.
	}

}
