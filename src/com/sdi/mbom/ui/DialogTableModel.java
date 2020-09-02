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
public class DialogTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632687768978148511L;

	/**
	 * 
	 */
	public DialogTableModel() {
	}

	/**
	 * @param rowCount
	 * @param columnCount
	 */
	public DialogTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public DialogTableModel(Vector<?> columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public DialogTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public DialogTableModel(Vector<?> data, Vector<?> columnNames) {
		super(data, columnNames);
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public DialogTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	//Set Not Editable Table
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    return false;
	}
}
