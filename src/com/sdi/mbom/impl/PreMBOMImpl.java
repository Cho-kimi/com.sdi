package com.sdi.mbom.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sdi.mbom.MBOMBuilder;
import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventHandler;
import com.sdi.mbom.MBOMChangeEventListener;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.PreMBOM;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;

public class PreMBOMImpl implements PreMBOM{
	
	private static final int MAX_THREAD_POOL = 5;
	
	private TCComponent sourceComponent;
	
	private MBOMLine topBOMLine;

	private MBOMBuilder owningBuilder;
	
	private List<MBOMLine> phantomMBOMLines;
	
	private List<MBOMChangeEventListener> mbomChangeEventlisteners;
	
	public PreMBOMImpl(MBOMBuilder owningBuilder, String topMBOMLineName, TCComponentBOMLine bomline, boolean b) {
		//초기화
		this.phantomMBOMLines = new ArrayList<MBOMLine>();
		this.mbomChangeEventlisteners = new CopyOnWriteArrayList<MBOMChangeEventListener>();
		
		this.owningBuilder = owningBuilder;
		this.sourceComponent = bomline;
		//최상위 BOMLine을 생성한다.(아직 BOM이 생성된 것은 아님)
		this.topBOMLine = owningBuilder.getHelper().generateMBOMLine(bomline, MBOMLine.NEW_ITEM_ID );
		this.topBOMLine.setMBOMChangeEventHandler(this);
	}

	@Override
	public MBOMLine getTopBOMLine() {
		return topBOMLine;
	}

	@Override
	public MBOMBuilder getOwningBuilder() {
		return this.owningBuilder;
	}

	@Override
	public TCComponent getSourceComponent() {
		return this.sourceComponent;
	}

	@Override
	public List<MBOMLine> getPhantomMBOMLines() {
		return this.phantomMBOMLines;
	}
	
	public void mbomLineUpdated(MBOMLine updatedLine) {
		fireMBOMChangeEvent(new MBOMChangeEvent(updatedLine));
	}
	
	@Override
	public MBOMChangeEventHandler getMBOMChangeEventHandler() {
		return this;
	}	

	@Override
	public synchronized List<MBOMChangeEventListener> getMBOMChangeEventListeners() {
		return this.mbomChangeEventlisteners;
	}

	@Override
	public synchronized void addMBOMChangeEventListener(MBOMChangeEventListener eventListener) {
		//한번만 등록하도록 처리
		if (getMBOMChangeEventListeners().indexOf(eventListener) == -1) {
			this.mbomChangeEventlisteners.add(eventListener);
		}
	}

	@Override
	public synchronized void removeMBOMChangeEventListener(MBOMChangeEventListener eventListener) {
		if (getMBOMChangeEventListeners().indexOf(eventListener) != -1) {
			this.mbomChangeEventlisteners.remove(eventListener);
		}
	}

	@Override
	public synchronized void fireMBOMChangeEvent(final MBOMChangeEvent event) {
		fireMBOMDataChangeEventByAsynch(event);
	}
	
	protected synchronized void fireMBOMDataChangeEventByAsynch(final MBOMChangeEvent event) {
		ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);

		//logger.info("[Event occurred : <{}> by <{}>]", new Object[] { event, caller.getName() });

		for (final MBOMChangeEventListener listener : getMBOMChangeEventListeners()) {
			executorService.execute(new Runnable() {
				public void run() {
						listener.onMBOMDataChanged(event);
				}
			});
		}
		executorService.shutdown();
	}

}
