/**
 * 
 */
package com.sdi.mbom.impl;

import com.sdi.mbom.TitledMBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMLine;

/**
 * @author cspark
 *
 */
public class TitledMBOMLineImpl extends MBOMLineImpl implements TitledMBOMLine {

	private String identyTitle;

	/**
	 * @param newObjectName
	 */
	public TitledMBOMLineImpl(String identyTitle, String newObjectName) {
		super(newObjectName);
		this.identyTitle = identyTitle;
	}

	/**
	 * @param newObjectName
	 * @param newItemId
	 */
	public TitledMBOMLineImpl(String identyTitle, String newObjectName, String newItemId) {
		super(newObjectName, newItemId);
		this.identyTitle = identyTitle;
	}

	/**
	 * @param permanentBOMLine
	 */
	public TitledMBOMLineImpl(String identyTitle, TCComponentBOMLine permanentBOMLine) {
		super(permanentBOMLine);
		this.identyTitle = identyTitle;
	}

	/**
	 * @param sourceBOMLine
	 * @param newItemId
	 * @param refPropertyNames
	 */
	public TitledMBOMLineImpl(String identyTitle, TCComponentBOMLine sourceBOMLine, String newItemId, String[] refPropertyNames) {
		super(sourceBOMLine, newItemId, refPropertyNames);
		this.identyTitle = identyTitle;
	}

	/**
	 * @param bomline
	 * @param isPermanent
	 * @param newObjectName
	 * @param newItemId
	 * @param refPropertyNames
	 */
	public TitledMBOMLineImpl(String identyTitle, TCComponentBOMLine bomline, boolean isPermanent, String newObjectName, String newItemId,
			String[] refPropertyNames) {
		super(bomline, isPermanent, newObjectName, newItemId, refPropertyNames);
		this.identyTitle = identyTitle;
	}

	@Override
	public String getTitleIdentity() {
		return this.identyTitle;
	}

	public void setTitleIdentity(String identyTitle) {
		this.identyTitle = identyTitle;
	}

}
