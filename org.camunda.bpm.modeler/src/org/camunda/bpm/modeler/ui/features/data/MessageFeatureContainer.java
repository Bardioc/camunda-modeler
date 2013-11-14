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
package org.camunda.bpm.modeler.ui.features.data;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2AddShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2MoveShapeFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.features.container.BaseElementFeatureContainer;
import org.camunda.bpm.modeler.core.features.data.AbstractCreateRootElementFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil.Envelope;
import org.camunda.bpm.modeler.ui.Images;
import org.camunda.bpm.modeler.ui.features.LayoutBaseElementTextFeature;
import org.camunda.bpm.modeler.ui.features.choreography.UpdateChoreographyMessageFlowFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Message;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class MessageFeatureContainer extends BaseElementFeatureContainer {

	public static final int ENVELOPE_WIDTH = 30;
	public static final int ENVELOPE_HEIGHT = 20;

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof Message;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateMessageFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractBpmn2AddShapeFeature<Message>(fp) {

			@Override
			public boolean canAdd(IAddContext context) {
				return true;
			}

			@Override
			protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
				
				Message msg = getBusinessObject(context);
				
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();

				int width = bounds.getWidth();
				int height = bounds.getHeight();
				int x = bounds.getX();
				int y = bounds.getY();
				
				ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
				Rectangle invisibleRect = gaService.createInvisibleRectangle(newShape);
				gaService.setLocationAndSize(invisibleRect, x, y, width, height);

				Envelope envelope = GraphicsUtil.createEnvelope(invisibleRect, 0, 0, width, height);
				envelope.rect.setFilled(true);
				StyleUtil.applyStyle(envelope.rect, msg);
				envelope.line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				return newShape;
			}

			@Override
			public int getDefaultHeight() {
				return ENVELOPE_HEIGHT;
			}

			@Override
			public int getDefaultWidth() {
				return ENVELOPE_WIDTH;
			}
			
			@Override
			protected boolean isCreateExternalLabel() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		// because ChoreographyTasks have an associated Message visual,
		// we need to allow these to update themselves also.
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateBaseElementNameFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateChoreographyMessageFlowFeature(fp));
		return multiUpdate;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutBaseElementTextFeature(fp) {
			@Override
			public int getMinimumWidth() {
				return 30;
			}
		};
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultBpmn2MoveShapeFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultResizeShapeFeature(fp) {
			@Override
			public boolean canResizeShape(IResizeShapeContext context) {
				return false;
			}
		};
	}

	public static class CreateMessageFeature extends AbstractCreateRootElementFeature<Message> {

		public CreateMessageFeature(IFeatureProvider fp) {
			super(fp, "Message", "Represents the content of a communication between two Participants");
		}

		@Override
		public String getStencilImageId() {
			return Images.IMG_16_MESSAGE;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getMessage();
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		return null;
	}
}