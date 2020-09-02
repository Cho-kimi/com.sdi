package com.sdi.mbom;

import java.util.List;

public interface PreMBOM extends MBOM {
	
	
	public MBOMBuilder getOwningBuilder();

	public List<MBOMLine> getPhantomMBOMLines();
	
}
