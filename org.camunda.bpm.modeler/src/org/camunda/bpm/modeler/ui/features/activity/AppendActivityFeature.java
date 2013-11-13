/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.camunda.bpm.modeler.ui.features.activity;

import org.camunda.bpm.modeler.ui.Images;
import org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;

/**
 * @author Bob Brodt
 *
 */
public class AppendActivityFeature extends AbstractAppendNodeNodeFeature<Activity> {

	/**
	 * @param fp
	 */
	public AppendActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Append Activity";
	}

	@Override
	public String getDescription() {
		return "Create a new Activity and connect it to this item";
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_TASK;
	}

	/* (non-Javadoc)
	 * @see org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getActivity();
	}
}
