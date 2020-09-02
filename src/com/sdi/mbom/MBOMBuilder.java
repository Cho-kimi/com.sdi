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
	 * @param permanent    		���ǵ� BOMLine component
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine permanent);
	
	/**
	 * 
	 * @param bomline    		BOMLine component
	 * @param isPermanent		bomline�� permanent����, true�̸� bomline�� permanent�̰� false�̸� source�� �Ǹ�, �������� �������� ������ BOMLine�� �űԷ� �����Ѵ�.
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine bomline, boolean isPermanent);
	

	/**
	 * 
	 * @param newObjectName		������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @return
	 */
	MBOMLine generateMBOMLine(String newObjectName);
	
	/**
	 * 
	 * @param newObjectName		������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @return
	 */
	MBOMLine generateMBOMLine(String newObjectName, String newItemId);
	
	
	/**
	 * isPermanent�� false�� �Ͽ� ����
	 * 
	 * @param sourceBomline     �ű� ������ BOMLine�� ���� BOMLine ������Ʈ (null�� �ƴҰ�� BOMLine �Ӽ��� �����ϰ� �ȴ�)
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newItemId);

	/**
	 * isPermanent�� false�� �Ͽ� ����
	 * 
	 * @param sourceBomline     �ű� ������ BOMLine�� ���� BOMLine ������Ʈ (null�� �ƴҰ�� BOMLine �Ӽ��� �����ϰ� �ȴ�)
	 * @param newObjectName     ������ ������� BOMLine�� �̸�, ���� null�̰� source�� ������� source�� item_name�� ���
	 * @param newItemId			�űԷ� ������ �������� ���̵�, isPermanent�� false�̰� newItemId���� null�� ��� �⺻������ MBOMLine.NEW_ITEM_ID�� ����
	 * @param refPropertyNames  ������ BOMLine���� ���� ������ �Ӽ��� �迭
	 * @return
	 */
	MBOMLine generateMBOMLine(TCComponentBOMLine sourceBomline, String newObjectName, String newItemId , String[] refPropertyNames);

	/**
	 * ������ �������� Object Name�� ��ȯ, suffix�� �־����� �ٿ��� ��ȯ
	 * 
	 * @param newName
	 * @param suffix
	 * @return
	 */
	String generateMBOMItemName(String newName, String suffix);
	
	
}
