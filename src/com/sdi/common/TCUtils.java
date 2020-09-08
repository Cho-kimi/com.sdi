/**
 * 
 */
package com.sdi.common;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentRevisionRule;
import com.teamcenter.rac.kernel.TCComponentRevisionRuleType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

/**
 * @author cspark
 *
 */
public class TCUtils {
	
	public static TCComponentItem createItem(TCSession session, String type, String item_id, String item_name) throws TCException
	{
		TCComponentItemType itemType = (TCComponentItemType)session.getTypeComponent(type);
		TCComponentItem item = itemType.create(item_id, "A", type, item_name, "", null);
		
		//�ش� ���� ��ġ�� �ִٸ� �ش� ������ �߰�
		session.getUser().getHomeFolder().add("contents", item);
		
		return item;
	}
	
	public static  TCComponentItem findItem(TCSession session, String type, String item_id) throws TCException
	{
		TCComponentItemType itemType = (TCComponentItemType)session.getTypeComponent(type);
		
		//Multi Field Key ������� ������ ID�� ������ �������� ���� �� ���� �Ͽ� �迭�� �޾ƿ;���
		//( ��Ȯ�� ���� �ش� ������������ ��Ȯ�� �Ͽ� �������� ���͸� �ϴ� ����� ����. ���� ��� ���� ù��ü ������ ��ȯ )
		TCComponentItem[] items = itemType.findItems(item_id);
		  
		if(items != null && items.length > 0) {
		  return items[0];
		}
		return null;
	}
	
	

	public static TCComponentRevisionRule getRevisionRule(TCSession session, TCComponentBOMWindow refBOMWindow) throws TCException
	{
		
		TCComponentRevisionRule revisionRule = null;
		
		if(refBOMWindow != null) {
			try {
				revisionRule = refBOMWindow.getRevisionRule();
			}catch(Throwable t) {
				t.printStackTrace();
			}
		}
		
		//������ ���� ��쿡�� ó��
		if(revisionRule == null) {
			TCComponentRevisionRuleType revisionRuleType = (TCComponentRevisionRuleType)getTypeComponent(session, ExtTCCostants.TYPE_REVISION_RULE, null);
			revisionRule = revisionRuleType.getDefaultRule();
		}
		
		return revisionRule;
		
	}

	public static TCComponentType getTypeComponent(TCSession session, String typeName, TCComponent instance) throws TCException
	{
		if(instance != null) {
			return instance.getTypeComponent();
		}
		return session.getTypeComponent(typeName);
	}

	public static TCSession getDefaultSession() {
		return  (TCSession)AIFUtility.getSessionManager().getDefaultSession();
	}

}
