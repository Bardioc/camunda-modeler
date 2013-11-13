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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.activity.AbstractCreateExpandableFlowNodeFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class AdHocSubProcessFeatureContainer extends AbstractExpandableActivityFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof AdHocSubProcess;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateAdHocSubProcessFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddExpandableActivityFeature<AdHocSubProcess>(fp) {
			@Override
			protected void postAddHook(IAddContext context, ContainerShape newShape) {
				super.postAddHook(context, newShape);
				
				GraphicsUtil.showActivityMarker(newShape, GraphicsUtil.ACTIVITY_MARKER_AD_HOC);
			}
		};
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		UpdateExpandableActivityFeature updateFeature = new UpdateExpandableActivityFeature(fp);
		multiUpdate.addUpdateFeature(updateFeature);
		return multiUpdate;
	}

	public static class CreateAdHocSubProcessFeature extends AbstractCreateExpandableFlowNodeFeature<AdHocSubProcess> {

		public CreateAdHocSubProcessFeature(IFeatureProvider fp) {
			super(fp, "Ad-Hoc Sub-Process",
					"A specialized sub-process in which the activities have no required sequence relationships");
		}

		@Override
		protected String getStencilImageId() {
			return Images.IMG_16_AD_HOC_SUB_PROCESS;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getAdHocSubProcess();
		}
	}
}