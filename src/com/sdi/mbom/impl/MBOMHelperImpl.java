/**
 * 
 */
package com.sdi.mbom.impl;

import com.sdi.mbom.MBOMHelper;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.TitledMBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMLine;

/**
 * @author cspark
 *
 */
public class MBOMHelperImpl implements MBOMHelper {
	

	public final String [] DEFAULT_BOMLINE_COPY_PROPNAMES = new String[] {"Reference Degignator"};
	
	private static MBOMHelper _singleton;
	
	private  String [] propNames;
	
	
	public static MBOMHelper getHelper() {
		if(_singleton == null) {
			_singleton = new MBOMHelperImpl();
		}
		return _singleton;
	}

	@Override
	public String[] getMBOMLineCopyPorpNames() {
		if(propNames == null) {
			propNames = DEFAULT_BOMLINE_COPY_PROPNAMES;
		}
		return propNames;
	}
	
	@Override
	public void setMBOMLineCopyPorpNames(String[] propNames) {
		if(propNames == null) {
			propNames = DEFAULT_BOMLINE_COPY_PROPNAMES;
		}
		this.propNames =  propNames;
	}
	
	
	
	
	/**
	 * 생성할 아이템의 Object Name을 반환, suffix가 주어지면 붙여서 반환
	 * 
	 * @param newName
	 * @param suffix
	 * @return
	 */	
	@Override
	public String generateMBOMItemName(String newName, String suffix) {
		if(suffix == null) return newName;
		return String.join("-", newName, suffix);
	}
	
	@Override
	public TitledMBOMLine generateTitledMBOMLine(String title, String newObjectName) {		
		return new TitledMBOMLineImpl(title, newObjectName);
	}
	
	/**
	 * 
	 * @param permanent    		정의된 BOMLine component
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine permanent) {	
		return new MBOMLineImpl(permanent);
	}
		
	/**
	 * 
	 * @param bomline    		BOMLine component
	 * @param isPermanent		bomline의 permanent여부, true이면 bomline이 permanent이고 false이면 source가 되며, 아이템은 생성하지 않으나 BOMLine은 신규로 생성한다.
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine bomline, boolean isPermanent ) {		
		return generateMBOMLine(bomline, null, null, getMBOMLineCopyPorpNames());
	}
	

	/**
	 * 
	 * @param newObjectName		새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(String newObjectName) {		
		return new MBOMLineImpl(newObjectName);
	}

	/**
	 * 
	 * @param newObjectName		새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(String newObjectName, String newItemId) {		
		return new MBOMLineImpl(newObjectName, newItemId);
	}

	/**
	 * isPermanent를 false로 하여 생성
	 * 
	 * @param sourceBomline     신규 생성될 BOMLine의 참조 BOMLine 컴포넌트 (null이 아닐경우 BOMLine 속성을 복사하게 된다)
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newItemId) {
		return generateMBOMLine(sourceBomline, null, newItemId, getMBOMLineCopyPorpNames());
	}

	/**
	 * isPermanent를 false로 하여 생성
	 * 
	 * @param sourceBomline     신규 생성될 BOMLine의 참조 BOMLine 컴포넌트 (null이 아닐경우 BOMLine 속성을 복사하게 된다)
	 * @param newObjectName     새로이 만들어질 BOMLine의 이름, 값이 null이고 source가 있을경우 source의 item_name을 사용
	 * @param newItemId			신규로 생성할 아이템의 아이디, isPermanent가 false이고 newItemId값이 null일 경우 기본값으로 MBOMLine.NEW_ITEM_ID가 사용됨
	 * @param refPropertyNames  원래의 BOMLine으로 부터 복사할 속성명 배열
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newObjectName, String newItemId, String[] refPropertyNames) {
		return new MBOMLineImpl(sourceBomline, false, newObjectName, newItemId, refPropertyNames);
	}
	

}
