package com.sdi.mbom;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponent;

public interface MBOMComponent {
	
	public boolean isPermanent();
	
	public TCComponent getTCComponent();
	
	public String getName();
	
	public String getProperty(String propName);
	
	public List<Object> getProperties(String [] propNames);
	
	
}
