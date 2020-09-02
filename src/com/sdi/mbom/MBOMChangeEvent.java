package com.sdi.mbom;

import java.util.Arrays;
import java.util.List;

public class MBOMChangeEvent {

	public static final String MBOM_DATA_CHANGED_EVENT = "MBOM_DATA_CHANGED";
	
	private String eventType;
	private List<MBOMLine> eventDatas;

	public MBOMChangeEvent(String eventType) {
		this(eventType, null);
	}
	
	public MBOMChangeEvent(MBOMLine eventData) {
		this(Arrays.asList(eventData));
	}
	
	public MBOMChangeEvent(List<MBOMLine> eventDatas) {
		this.eventType = MBOM_DATA_CHANGED_EVENT;
		this.eventDatas = eventDatas;
	}
	
	public MBOMChangeEvent(String eventType, List<MBOMLine> eventDatas) {
		this.eventType = eventType;
		this.eventDatas = eventDatas;
	}

	public String getEventType() {
		return eventType;
	}

	public List<MBOMLine> getEventDatas() {
		return eventDatas;
	}
	
	
	
}
