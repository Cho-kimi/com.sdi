package com.sdi.commands.addMBom;

import com.sdi.mbom.ui.AddMBomFormDialog;
import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.Registry;


public class AddMBomFormCommand extends AbstractAIFCommand {
	
	public AddMBomFormCommand() throws Exception {
	  
		AbstractAIFUIApplication currentApp = AIFUtility.getActiveDesktop().getCurrentApplication();
		InterfaceAIFComponent targetComp = currentApp.getTargetComponent();
		
		Registry registry = Registry.getRegistry(com.sdi.mbom.MBOM.class);
		
		try {
			
			if(targetComp != null) {
				if(targetComp instanceof TCComponentBOMLine || targetComp instanceof TCComponentItem || targetComp instanceof TCComponentItemRevision) {
					
					TCComponentBOMLine topBOMLine  =(TCComponentBOMLine)targetComp;
					
					if(topBOMLine.getChildrenCount() < 1) {
						throw new Exception(registry.getString("MESSAGE_MBOM_NO_CHILD_BELOW_SELECTED_BOMLINE", "������ TOP �����ۿ� MBOM ���� ������ ���� ������Ʈ�� �������� �ʽ��ϴ�.")  );
					}
					
					//�ӽ÷� ������ M-BOM�� ȭ�鿡 ǥ���ϰ� ���� ���θ� Ȯ���մϴ�.
					AddMBomFormDialog dialog = new AddMBomFormDialog(AIFUtility.getActiveDesktop());
					dialog.setModal(true);
					dialog.setTarget(topBOMLine);
					setRunnable(dialog);
					
				}else {
					throw new Exception(registry.getString("MESSAGE_MBOM_INVALID_TYPE_FOR_TOP_ITEM", "MBOM ������ ���� TOP �������� ������ ���� �ʽ��ϴ�.")   + targetComp.getClass().getName() );
				}
			}else {
				throw new Exception(registry.getString("MESSAGE_MBOM_NEED_SELECTION_BOMLINE", "MBOM ������ ���� TOP ������ BOMLine�� �����Ͽ� �ֽʽÿ�"));
			}
		}catch(Throwable e) {
			String message = e.getMessage();
			MessageBox.post(message, registry.getString("TITLE_MBOM_COMMAND_ERROR", "M-BOM Pre Build ERROR "),  MessageBox.ERROR);
		}
	}
}
