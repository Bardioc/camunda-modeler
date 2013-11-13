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

package org.camunda.bpm.modeler.ui.features.gateway;

import org.camunda.bpm.modeler.ui.Images;
import org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;

/**
 * @author Bob Brodt
 *
 */
public class AppendGatewayFeature extends AbstractAppendNodeNodeFeature<Gateway> {

	@Override
	public boolean isAvailable(IContext context) {
		if (!(context instanceof ICustomContext)) {
			return super.isAvailable(context);
		}
		
		ICustomContext customContext = (ICustomContext) context;
		
		boolean isCompensationBoundaryEvent = isCompensationBoundaryEvent(customContext);
		
		if (isCompensationBoundaryEvent) {
			return false;
		}
		
		return super.isAvailable(context); 
	}
	
	/**
	 * @param fp
	 */
	public AppendGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Append Gateway";
	}

	@Override
	public String getDescription() {
		return "Create a new Gateway and connect it to this item";
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_EXCLUSIVE_GATEWAY;
	}

	/* (non-Javadoc)
	 * @see org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getGateway();
	}
}
