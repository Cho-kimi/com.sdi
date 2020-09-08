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


public class AddMBomFormCommand extends AbstractAIFCommand {
	
	public AddMBomFormCommand() throws Exception {
	  
		AbstractAIFUIApplication currentApp = AIFUtility.getActiveDesktop().getCurrentApplication();
		InterfaceAIFComponent targetComp = currentApp.getTargetComponent();
		
		try {
			
			if(targetComp != null) {
				if(targetComp instanceof TCComponentBOMLine || targetComp instanceof TCComponentItem || targetComp instanceof TCComponentItemRevision) {
					
					TCComponentBOMLine topBOMLine  =(TCComponentBOMLine)targetComp;
					
					if(topBOMLine.getChildrenCount() < 1) {
						throw new Exception("������ TOP �����ۿ� MBOM ���� ������ ���� ������Ʈ�� �������� �ʽ��ϴ�.");
					}
					
					//�ӽ÷� ������ M-BOM�� ȭ�鿡 ǥ���ϰ� ���� ���θ� Ȯ���մϴ�.
					AddMBomFormDialog dialog = new AddMBomFormDialog(AIFUtility.getActiveDesktop());
					dialog.setModal(true);
					dialog.setTarget(topBOMLine);
					setRunnable(dialog);
					
				}else {
					throw new Exception("MBOM ������ ���� TOP �������� ������ ���� �ʽ��ϴ�." + targetComp.getClass().getName() );
				}
			}else {
				throw new Exception("MBOM ������ ���� TOP ������ BOMLine�� �����Ͽ� �ֽʽÿ�");
			}
		}catch(Throwable e) {
			String message = e.getMessage();
			MessageBox.post(message, "M-BOM Pre Build ERROR ",  MessageBox.ERROR);
		}
	}
}
