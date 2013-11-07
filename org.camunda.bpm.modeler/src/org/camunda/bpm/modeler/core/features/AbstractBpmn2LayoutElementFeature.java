package org.camunda.bpm.modeler.core.features;

import static org.camunda.bpm.modeler.core.utils.ContextUtil.is;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * Default layout feature for all graphical elements
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractBpmn2LayoutElementFeature<T extends PictogramElement> extends AbstractLayoutFeature {

	public AbstractBpmn2LayoutElementFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean layout(ILayoutContext context) {
		adjustLabelPosition(context);
		
		return true;
	}

	/**
	 * Adjust label position if needed
	 * @param context
	 */
	protected void adjustLabelPosition(ILayoutContext context) {
		T pictogramElement = getLayoutedElement(context);
		
		if (isAdjustLabelPosition(context)) {
			// move label after the element has been moved
			moveLabel(pictogramElement);
		}
	}

	protected boolean isAdjustLabelPosition(ILayoutContext context) {
		return is(context, PropertyNames.LAYOUT_ADJUST_LABEL);
	}
	
	/**
	 * Move label for the corresponding pictogram element
	 * 
	 * @param element
	 */
	protected void moveLabel(T element) {
		
		ContainerShape labelShape = LabelUtil.getLabelShape(element, getDiagram());

		// we check if the label is selected itself.
		// if so, the movement will be (or was) handled by the labels move feature
		if (isEditorSelection(labelShape)) {
			return;
		} else {
			// movement needs to be done by us
			LabelUtil.repositionLabel(element, getFeatureProvider());
		}
	}
	
	/**
	 * Returns true if the given shape is currently selected in the editor
	 * 
	 * @param pictogramElement
	 * @return
	 */
	protected boolean isEditorSelection(PictogramElement pictogramElement) {
		List<PictogramElement> selection = getEditorSelection();
		return selection.contains(pictogramElement);
	}

	/**
	 * Return the editor selection
	 * 
	 * @return
	 */
	protected List<PictogramElement> getEditorSelection() {
		return Arrays.asList(getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements());
	}
	
	protected void updateDi(PictogramElement pictogramElement) {
		DIUtils.updateDI(pictogramElement);
	}
	
	@SuppressWarnings("unchecked")
	protected T getLayoutedElement(ILayoutContext context) {
		try {
			return (T) context.getPictogramElement();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(String.format("Illegal request to layout <%s>", context), e);
		}
	}
	
	protected BaseElement getLinkedBaseElement(PictogramElement shape) {
		return BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);
	}
	
	protected BPMNShape getLinkedBPMNShape(PictogramElement shape) {
		return BusinessObjectUtil.getFirstElementOfType(shape, BPMNShape.class);
	}
}
