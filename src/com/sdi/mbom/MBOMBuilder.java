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
	 * @param name                새로이 만들어질 BOMLine의 이름
	 * @param comp                원래의 BOMLine 아이템
	 * @param refPropertyNames  , 원래의 BOMLine으로 부터 복사할 속성명 배열
	 * @return
	 */
	MBOMLine generateMBOMLine(String targetItemId, TCComponentBOMLine target, TCComponentBOMLine source,
			String[] refPropertyNames);
	
	
}
