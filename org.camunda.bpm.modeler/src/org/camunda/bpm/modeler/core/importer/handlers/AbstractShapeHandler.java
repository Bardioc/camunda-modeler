/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer.handlers;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public abstract class AbstractShapeHandler<T extends BaseElement> extends AbstractDiagramElementHandler<T> {
	
	public AbstractShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	public final PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container) {
		if (diagramElement instanceof BPMNShape) {
			return handleShape(bpmnElement, (BPMNShape) diagramElement, container);
		} else {
			throw new IllegalArgumentException("Handling instances of BPMNShape only");
		}
	}
	
	/**
	 * Find a Graphiti feature for given shape and generate necessary diagram elements.
	 * 
	 * @param bpmnElement the element to find and create the diagram element for
	 * @param bpmnShape the shape of the diagram element
	 * @param container the container element to add visual element to
	 * 
	 * @return the generated picture
	 */
	public PictogramElement handleShape(T bpmnElement, BPMNShape bpmnShape, ContainerShape container) {

		AddContext context = createAddContext(bpmnElement);		
		IAddFeature addFeature = createAddFeature(context);
		
		if (addFeature != null && container != null) { // DI for parent e.g. participant might be missing

			// find the actual container (position wise) for the given parent container
			container = getActualContainingContainer(bpmnShape, container);
			
			setSize(context, bpmnShape, bpmnElement, container);
			
			setLocation(context, container, bpmnShape);
			
			addToTargetContainer(context, container);
			
			PictogramElement pictogramElement = createPictogramElement(bpmnElement, context, addFeature);
			
			return pictogramElement;
			
		} else {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Element not supported: "
					+ bpmnElement.eClass().getName()));
			
			return null;
			
		}
			
	}

	protected ContainerShape getActualContainingContainer(BPMNShape shape, ContainerShape container) {

		if (isExpanded(container)) {
			while (!isPositionallyContained(shape, container)) {
				if (container instanceof Diagram) {
					break;
				}

				container = container.getContainer();
			}
		}

		return container;
	}

	protected boolean isExpanded(ContainerShape container) {
		return FeatureSupport.isExpanded(container);
	}

	private boolean isPositionallyContained(BPMNShape shape, ContainerShape container) {
		ILocation containerLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(container);

		GraphicsAlgorithm graphics = container.getGraphicsAlgorithm();

		int containerHeight = graphics.getHeight();
		int containerWidth = graphics.getWidth();
		
		Bounds shapeBounds = shape.getBounds();
		
		// truncate shape coordinates 
		
		int shapeTopLeftX = (int) shapeBounds.getX();
		int shapeTopLeftY = (int) shapeBounds.getY();
		
		int shapeBottomRightX = (int) shapeBounds.getX() + (int) shapeBounds.getWidth();
		int shapeBottomRightY = (int) shapeBounds.getY() + (int) shapeBounds.getHeight();
		
		int shapeBottomLeftX = (int) shapeBounds.getX();
		int shapeBottomLeftY = (int) shapeBounds.getY() + (int) shapeBounds.getHeight();

		int shapeTopRightX = (int) shapeBounds.getX() + (int) shapeBounds.getWidth();
		int shapeTopRightY = (int) shapeBounds.getY();
		
		// check if top left point of shape is contained in container bounds
		// check with margin of 1
		IRectangle containerRect = ConversionUtil.rectangle(containerLocation.getX()-1, containerLocation.getY()-1, containerWidth +1, containerHeight+1);
		
		if (LayoutUtil.isContained(containerRect, ConversionUtil.location(shapeTopLeftX, shapeTopLeftY)) ||
			LayoutUtil.isContained(containerRect, ConversionUtil.location(shapeBottomRightX, shapeBottomRightY)) ||
			LayoutUtil.isContained(containerRect, ConversionUtil.location(shapeBottomLeftX, shapeBottomLeftY)) ||
			LayoutUtil.isContained(containerRect, ConversionUtil.location(shapeTopRightX, shapeTopRightY)) ) {
			return true;
		}
		
		return false;
	}

	protected PictogramElement createPictogramElement(T bpmnElement, AddContext context, IAddFeature addFeature) {
		
		if (addFeature.canAdd(context)) {
			return createPictogramElement(context, addFeature);
		} else { 
			String message = String.format("Add feature <%s> cannot add context <%s>", addFeature.getClass().getName(), context);
			
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, message));
			return null;
		}
		
	}

	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		
		Bounds bounds = shape.getBounds();
		
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		
		ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(container);
		x -= loc.getX();
		y -= loc.getY();

		context.setLocation(x, y);
	}

	protected void addToTargetContainer(AddContext context, ContainerShape container) {
		context.setTargetContainer(container);
	}

	protected void setSize(AddContext context, BPMNShape shape, T bpmnElement, ContainerShape targetContainer) {

		Bounds bounds = shape.getBounds();
		
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		
		context.setSize(width, height);		
		
	}

	protected IAddFeature createAddFeature(AddContext context) {
		return featureProvider.getAddFeature(context);
	}

	protected AddContext createAddContext(BaseElement bpmnElement) {
		AddContext context = new AddContext(new AreaContext(), bpmnElement);

		// MUST be set to make the layout mechanisms work
		ContextUtil.set(context, DIUtils.IMPORT);
		
		return context;
	}
	
	protected PictogramElement createPictogramElement(AddContext context, IAddFeature addFeature) {
		return addFeature.add(context);
	}
}
