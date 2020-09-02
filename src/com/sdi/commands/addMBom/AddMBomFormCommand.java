package com.sdi.commands.addMBom;

import java.awt.Frame;

import com.sdi.mbom.MBOMBuilder;
import com.sdi.mbom.MBOMManager;
import com.sdi.mbom.PreMBOM;
import com.sdi.mbom.ui.AddMBomFormDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.MessageBox;


public class AddMBomFormCommand extends AbstractAIFCommand {
	
	private MBOMBuilder mbomBuilder;
	
	
	public AddMBomFormCommand() throws Exception {
	  
		AbstractAIFUIApplication currentApp = AIFUtility.getActiveDesktop().getCurrentApplication();
		InterfaceAIFComponent targetComp = currentApp.getTargetComponent();
		
		try {
			if(targetComp != null) {
				if(targetComp instanceof TCComponentBOMLine || targetComp instanceof TCComponentItem || targetComp instanceof TCComponentItemRevision) {
					PreMBOM preMBOM = getMbomBuilder().buildPreMBOM(targetComp);
					
//					if(preMBOM == null || preMBOM.getChildrenCount() < 1) {
//						throw new Exception("������ TOP �����ۿ� MBOM ���� ������ ���� ������Ʈ�� �������� �ʽ��ϴ�.");
//					}
					
					//�ӽ÷� ������ M-BOM�� ȭ�鿡 ǥ���ϰ� ���� ���θ� Ȯ���մϴ�.
					AddMBomFormDialog dialog = new AddMBomFormDialog(AIFUtility.getActiveDesktop());
					dialog.setMBOM(preMBOM);
					dialog.setModal(true);
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


	public MBOMBuilder getMbomBuilder() {
		
		if(mbomBuilder == null) {
			mbomBuilder = MBOMManager.getBuilder();
		}
		return mbomBuilder;
	}
}
