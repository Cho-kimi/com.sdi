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
	 * ������ �������� Object Name�� ��ȯ, suffix�� �־����� �ٿ��� ��ȯ
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
	 * @param permanent    		���ǵ� BOMLine component
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine permanent) {	
		return new MBOMLineImpl(permanent);
	}
		
	/**
	 * 
	 * @param bomline    		BOMLine component
	 * @param isPermanent		bomline�� permanent����, true�̸� bomline�� permanent�̰� false�̸� source�� �Ǹ�, �������� �������� ������ BOMLine�� �űԷ� �����Ѵ�.
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine bomline, boolean isPermanent ) {		
		return generateMBOMLine(bomline, null, null, getMBOMLineCopyPorpNames());
	}
	

	/**
	 * 
	 * @param newObjectName		������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(String newObjectName) {		
		return new MBOMLineImpl(newObjectName);
	}

	/**
	 * 
	 * @param newObjectName		������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(String newObjectName, String newItemId) {		
		return new MBOMLineImpl(newObjectName, newItemId);
	}

	/**
	 * isPermanent�� false�� �Ͽ� ����
	 * 
	 * @param sourceBomline     �ű� ������ BOMLine�� ���� BOMLine ������Ʈ (null�� �ƴҰ�� BOMLine �Ӽ��� �����ϰ� �ȴ�)
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newItemId) {
		return generateMBOMLine(sourceBomline, null, newItemId, getMBOMLineCopyPorpNames());
	}

	/**
	 * isPermanent�� false�� �Ͽ� ����
	 * 
	 * @param sourceBomline     �ű� ������ BOMLine�� ���� BOMLine ������Ʈ (null�� �ƴҰ�� BOMLine �Ӽ��� �����ϰ� �ȴ�)
	 * @param newObjectName     ������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @param refPropertyNames  ������ BOMLine���� ���� ������ �Ӽ��� �迭
	 * @return
	 */
	@Override
	public MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newObjectName, String newItemId, String[] refPropertyNames) {
		return new MBOMLineImpl(sourceBomline, false, newObjectName, newItemId, refPropertyNames);
	}
	

}
