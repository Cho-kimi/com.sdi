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

	MBOMLine generateMBOMLine(String name, TCComponentBOMLine permanent, TCComponentBOMLine source);

	/**
	 * 
	 * @param newObjectName       ������ ������� BOMLine�� �̸�
	 * @param permanent           �̹� ��������ִ� BOMLine ������Ʈ
	 * @param source              ������ BOMLine ������Ʈ
	 * @param refPropertyNames  , ������ BOMLine���� ���� ������ �Ӽ��� �迭
	 * @return
	 */
	MBOMLine generateMBOMLine(String newObjectName, TCComponentBOMLine permanent, TCComponentBOMLine source,
			String[] refPropertyNames);
	
	
}
