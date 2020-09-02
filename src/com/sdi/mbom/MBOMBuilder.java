/**
 * 
 */
package com.sdi.mbom;

import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;

/**
 * @author cspark
 *
 */
public interface MBOMBuilder {

	PreMBOM buildPreMBOM(InterfaceAIFComponent targetComp);
	
	void updateBOM(MBOM mbom);
	
	void buildBOM(PreMBOM mbom);

	MBOMLine generateMBOMLine(String name);

	MBOMLine generateMBOMLine(String name, TCComponentBOMLine source);

	MBOMLine generateMBOMLine(String name, TCComponentBOMLine target, TCComponentBOMLine source);

	/**
	 * 
	 * @param name                ������ ������� BOMLine�� �̸�
	 * @param comp                ������ BOMLine ������
	 * @param refPropertyNames  , ������ BOMLine���� ���� ������ �Ӽ��� �迭
	 * @return
	 */
	MBOMLine generateMBOMLine(String targetItemId, TCComponentBOMLine target, TCComponentBOMLine source,
			String[] refPropertyNames);
	
	
}
