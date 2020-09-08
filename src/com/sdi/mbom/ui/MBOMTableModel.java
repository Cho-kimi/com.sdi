/**
 * 
 */
package com.sdi.mbom.ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author 
 *
 */
public class MBOMTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632687768978148511L;

	/**
	 * 
	 */
	public MBOMTableModel() {
	}

	/**
	 * @param rowCount
	 * @param columnCount
	 */
	public MBOMTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public MBOMTableModel(Vector<String> columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public MBOMTableModel(String[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public MBOMTableModel(Vector<MBOMLineTableRow> data, Vector<String> columnNames) {
		super(data, columnNames);
	}
	
	public MBOMLineTableRow getRowAt(int row) {
		return (MBOMLineTableRow)this.dataVector.elementAt(row);
	}
	
	//Set Not Editable Table
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    return false;
	}

	public void clearData() {
		this.getDataVector().clear();
		this.fireTableDataChanged();
	}
}
