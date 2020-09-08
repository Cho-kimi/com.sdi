package com.sdi.handlers;

import java.lang.reflect.Constructor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFCommand;

/**
 * @Copyright : KIMIES
 * @author   : ������
 * @since    : 2020. 08. 30
 * Package ID : com.sdi.handlers.CommonHandler.java
 * 
 * 1.Command Class �� �����ڴ� public ���� �Ѵ�.
 * 2.Command Class �� �����ڴ� String parameter �ϳ��� �������� �Ѵ�.
 * 3.Command ID = Command Class�� Ǯ ��Ű�� �� + Ŭ���� �̸�
 * ex) Command Class �� co.kr.kimi.newprocess.NewProcessCommand.java �� ��� Command ID �� "co.kr.kimi.newprocess.NewProcessCommand" �� �Ǿ� �� �Ѵ�.
 * 4.Command Class ���� �ݵ�� Default �����ڰ� ���� �Ͽ��� �Ѵ�. 
 */
public class CommonHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			
			String commandInfo = event.getCommand().getId();
			
			Class<?> commandClass = Class.forName(commandInfo);
			
			Constructor<?> constructor = commandClass.getConstructor() ;

			AbstractAIFCommand aifCommand = (AbstractAIFCommand) constructor.newInstance() ;
			aifCommand.executeModal();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}