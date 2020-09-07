/**
 * 
 */
package com.sdi.mbom.ui;

import java.awt.Container;

import javax.swing.text.Document;

import com.sdi.mbom.PropertyUIProvider;
import com.teamcenter.rac.util.iTextField;

/**
 * @author cspark
 *
 */
public class PropertyProvideField extends iTextField implements PropertyUIProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784576833367470482L;

	/**
	 * @param arg0
	 */
	public PropertyProvideField(Container arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PropertyProvideField(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PropertyProvideField(int arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PropertyProvideField(String arg0, Container arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PropertyProvideField(int arg0, boolean arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PropertyProvideField(int arg0, Container arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PropertyProvideField(String arg0, int arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public PropertyProvideField(int arg0, int arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public PropertyProvideField(String arg0, int arg1, Container arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public PropertyProvideField(Document arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public PropertyProvideField(Document arg0, String arg1, int arg2, Container arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	public PropertyProvideField(Document arg0, String arg1, int arg2, int arg3, boolean arg4, Container arg5) {
		super(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public boolean isSupprtProprty(String proeprtyName) {
		return true;
	}

	@Override
	public String getPropertyValue() {
		return getText();
	}

	@Override
	public void setPropertyValue(String value) {
		setText(value);

	}

}
