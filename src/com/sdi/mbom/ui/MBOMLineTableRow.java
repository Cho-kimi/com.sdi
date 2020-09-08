package com.sdi.mbom.ui;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.sdi.mbom.MBOMLine;

public class MBOMLineTableRow extends Vector<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2664023824316892887L;
	
	private MBOMLine mbomLine;

	private Vector<String> visibleColumn;
	
	private String [] columnNames;

	public MBOMLineTableRow(Collection<? extends Object> c) {
		super(c);
	}

	public MBOMLineTableRow(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}

	public MBOMLineTableRow(int initialCapacity) {
		super(initialCapacity);
	}
	
	
    public MBOMLineTableRow(MBOMLine mbomLine, String[] columnNames) {
		super((columnNames == null)? 0: columnNames.length);
		
		this.columnNames = columnNames;
		this.setVisibleColumn(columnNames);
		this.setMBOMLine(mbomLine);
	}

	private static Vector<String> nonNullVector(Vector<String> v) {
        return (v != null) ? v : new Vector<String>();
    }
	
	
	private static Vector<String> nonNullVector(String [] a) {
		
		Vector<String> v = new Vector<String>();
		if(a != null) {
			for(String vale : a) {
				v.add(vale);
			}
		}
        return v;
    }
	
	public void setMBOMLine(MBOMLine mbomLine) {
		this.mbomLine = mbomLine;
		updateMBOMLine();
	}
	
	public void updateMBOMLine() {
		if(this.mbomLine != null) {
			this.removeAllElements();
			List<Object> properties = this.mbomLine.getProperties(getVisibleColumnNames());
			addAll(properties);
		}
	}
	
	/**
	 * @return the primitiveData
	 */
	public MBOMLine getMBOMLine() {
		return mbomLine;
	}

	/**
	 * @return the visibleColumn
	 */
	public Vector<String> getVisibleColumn() {
		return visibleColumn;
	}
	
	/**
	 * @return the visibleColumn
	 */
	public String[] getVisibleColumnNames() {
		
		if(this.columnNames == null || this.visibleColumn.size()  != columnNames.length) {
			this.columnNames = toArray(new String[this.visibleColumn.size()]);
		}

		return this.columnNames;
	}
	

	/**
	 * @param visibleColumn the visibleColumn to set
	 */
	public void setVisibleColumn(Vector<String> visibleColumn) {
		this.visibleColumn = nonNullVector(visibleColumn);
		if(this.columnNames == null || this.visibleColumn.size()  != columnNames.length) {
			this.columnNames = toArray(new String[this.visibleColumn.size()]);
		}
	}
	
	/**
	 * @param visibleColumn the visibleColumn to set
	 */
	public void setVisibleColumn(String [] columnNames) {
		this.columnNames = columnNames;
		this.visibleColumn = nonNullVector(this.columnNames);
	}

	@Override
	public synchronized Object elementAt(int index) {
		Object value = super.elementAt(index);
		return value;
	}

	@Override
	public synchronized Object get(int index) {
		Object value = super.get(index);
		return value;
	}
	
	
	
	

}
