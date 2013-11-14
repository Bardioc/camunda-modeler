package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.widgets.Text;

public abstract class ModelAttributeTextBinding<V> extends ModelTextBinding<V> {
	
	protected EStructuralFeature feature;
	
	public ModelAttributeTextBinding(EObject model, EStructuralFeature feature, Text control) {
		super(model, control);
		
		this.feature = feature;
	}
	
	@Override
	protected void ensureChangeSupportAdded() {
		EAttributeChangeSupport.ensureAdded(model, feature, control);
	}
	
	/**
	 * Retrieves the model value from an attribute
	 * 
	 * @return the view value
	 */
	@Override
	public V getModelValue() {
		try {
			return (V) model.eGet(feature);
		} catch (Exception e) {
			// FIXME whats causing this
			throw new IllegalArgumentException("Could not get feature "+ feature +" for " + model, e);
		}
	}

	/**
	 * Sets the model value to the specified argument
	 * 
	 * @param value the value to update the model with
	 */
	public void setModelValue(V value)  {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		ModelUtil.setValue(domain, model, feature, value);
	}
}
