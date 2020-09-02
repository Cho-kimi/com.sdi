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
//						throw new Exception("선택한 TOP 아이템에 MBOM 구성 가능한 하위 컴포넌트가 존재하지 않습니다.");
//					}
					
					//임시로 구성된 M-BOM을 화면에 표시하고 생성 여부를 확인합니다.
					AddMBomFormDialog dialog = new AddMBomFormDialog(AIFUtility.getActiveDesktop());
					dialog.setMBOM(preMBOM);
					dialog.setModal(true);
					setRunnable(dialog);
				}else {
					throw new Exception("MBOM 생성을 위한 TOP 아이템의 형식이 맞지 않습니다." + targetComp.getClass().getName() );
				}
			}else {
				throw new Exception("MBOM 생성을 위한 TOP 아이템 BOMLine을 선택하여 주십시요");
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
