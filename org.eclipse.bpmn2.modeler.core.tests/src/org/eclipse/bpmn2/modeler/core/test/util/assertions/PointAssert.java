/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.bpmn2.modeler.core.layout.util.Vector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

public class PointAssert extends AbstractAssert<PointAssert, Point> {

	protected PointAssert(Point actual) {
		super(actual, PointAssert.class);
	}

	public PointAssert isEqualTo(int x, int y) {
		return isEqualTo(point(x, y));
	}
	
	public PointAssert isEqualTo(Point expected) {
		return isEqualTo(expected, 0);
	}
	
	public PointAssert isEqualTo(Point expected, int tolerance) {
		
		if (actual == null) {
			Assertions.fail(String.format("Expected actual to equal <%s> but was <null>", expected));
		}
		
		if (!Vector.equal(actual, expected, tolerance)) {
			Assertions.fail(String.format("Expected actual to equal <Point(%s, %s)> but was <Point(%s, %s)>", expected.getX(), expected.getY(), actual.getX(), actual.getY()));
		}
		
		return myself;
	}
}
