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

	/**
	 * 
	 * @param permanent    		정의된 BOMLine component
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine permanent);
	
	/**
	 * 
	 * @param bomline    		BOMLine component
	 * @param isPermanent		bomline의 permanent여부, true이면 bomline이 permanent이고 false이면 source가 되며, 아이템은 생성하지 않으나 BOMLine은 신규로 생성한다.
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine bomline, boolean isPermanent);
	

	/**
	 * 
	 * @param newObjectName		새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @return
	 */
	MBOMLine generateMBOMLine(String newObjectName);
	
	/**
	 * 
	 * @param newObjectName		새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @return
	 */
	MBOMLine generateMBOMLine(String newObjectName, String newItemId);
	
	
	/**
	 * isPermanent를 false로 하여 생성
	 * 
	 * @param sourceBomline     신규 생성될 BOMLine의 참조 BOMLine 컴포넌트 (null이 아닐경우 BOMLine 속성을 복사하게 된다)
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newItemId);

	/**
	 * isPermanent를 false로 하여 생성
	 * 
	 * @param sourceBomline     신규 생성될 BOMLine의 참조 BOMLine 컴포넌트 (null이 아닐경우 BOMLine 속성을 복사하게 된다)
	 * @param newObjectName     새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @param refPropertyNames  원래의 BOMLine으로 부터 복사할 속성명 배열
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newObjectName, String newItemId , String[] refPropertyNames);

	/**
	 * 생성할 아이템의 Object Name을 반환, suffix가 주어지면 붙여서 반환
	 * 
	 * @param newName
	 * @param suffix
	 * @return
	 */
	String generateMBOMItemName(String newName, String suffix);
	
	
}
