package com.sdi.handlers;

import java.lang.reflect.Constructor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFCommand;

/**
 * @Copyright : KIMIES
 * @author   : 조성현
 * @since    : 2020. 08. 30
 * Package ID : com.sdi.handlers.CommonHandler.java
 * 
 * 1.Command Class 의 생성자는 public 으로 한다.
 * 2.Command Class 의 생성자는 String parameter 하나를 가지도록 한다.
 * 3.Command ID = Command Class의 풀 패키지 명 + 클래스 이름
 * ex) Command Class 가 co.kr.kimi.newprocess.NewProcessCommand.java 인 경우 Command ID 는 "co.kr.kimi.newprocess.NewProcessCommand" 가 되어 야 한다.
 * 4.Command Class 에는 반드시 Default 생성자가 존재 하여야 한다. 
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