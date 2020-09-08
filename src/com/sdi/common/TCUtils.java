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
		
		//해당 폴더 위치가 있다면 해당 폴더에 추가
		session.getUser().getHomeFolder().add("contents", item);
		
		return item;
	}
	
	public static  TCComponentItem findItem(TCSession session, String type, String item_id) throws TCException
	{
		TCComponentItemType itemType = (TCComponentItemType)session.getTypeComponent(type);
		
		//Multi Field Key 기능으로 동일한 ID를 가지는 아이템이 있을 수 있음 하여 배열로 받아와야함
		//( 정확한 것은 해당 아이템유형을 정확히 하여 유형으로 필터링 하는 방법도 있음. 없을 경우 제일 첫번체 아이템 반환 )
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
		
		//오류가 났을 경우에도 처리
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
