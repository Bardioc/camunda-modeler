package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.test.util.operations.Operation;
import org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IResizeFeature;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class ResizeParticipantFeatureTest extends AbstractResizeFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldNotRepositionFlowElementLabel() {
		
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "InclusiveGateway_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		Point shrinkAmount = point(0, 50);
		
		IRectangle preResizeLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.fromTopLeftBy(shrinkAmount)
			.execute();
		
		// then
		assertThat(elementLabelShape)
			.bounds()
				.isEqualTo(preResizeLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldNotRepositionBoundaryEventLabel() {
		
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		Point shrinkAmount = point(0, 50);
		
		IRectangle preResizeLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.fromTopLeftBy(shrinkAmount)
			.execute();

		// then
		assertThat(elementLabelShape)
			.bounds()
				.isEqualTo(preResizeLabelBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantTop() {

		// y = 50 is allowed
		assertResize("Participant_1", point(0, 50), Sector.TOP_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("Participant_1", point(0, 60), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantBottom() {

		// y = 50 is allowed
		assertResize("Participant_1", point(0, -50), Sector.BOTTOM_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("Participant_1", point(0, -60), Sector.BOTTOM_LEFT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantWithFlowElementsTop() {

		// y = 50 is allowed
		assertResize("_Participant_3", point(0, 50), Sector.TOP_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("_Participant_3", point(0, 60), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantWithFlowElementsBottom() {

		// y = 50 is allowed
		assertResize("_Participant_3", point(0, -50), Sector.BOTTOM_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("_Participant_3", point(0, -60), Sector.BOTTOM_LEFT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantComplexTop() {

		// y = 70 is allowed
		assertResize("Participant_2", point(0, 70), Sector.TOP_LEFT);
		
		// y = 80 makes participant too small
		assertNoResize("Participant_2", point(0, 80), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeParticipantComplexBottom() {

		// y = 40 is allowed
		assertResize("Participant_1", point(0, -40), Sector.BOTTOM_LEFT);
		
		// y = 50 makes participant too small
		assertNoResize("Participant_1", point(0, -50), Sector.BOTTOM_LEFT);
	}
}
