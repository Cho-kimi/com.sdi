/**
 * 
 */
package com.sdi.mbom;

import java.util.List;

/**
 * @author cspark
 *
 */
public interface MBOMChangeEventHandler {
	
	List<MBOMChangeEventListener> getMBOMChangeEventListeners();

	void addMBOMChangeEventListener(MBOMChangeEventListener eventListener) ;

	void removeMBOMChangeEventListener(MBOMChangeEventListener eventListener);

	void fireMBOMChangeEvent(MBOMChangeEvent event);

}
