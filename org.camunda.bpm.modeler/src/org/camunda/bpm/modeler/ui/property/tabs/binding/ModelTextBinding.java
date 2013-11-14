package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Establishes a simple model to text binding
 * 
 * @author nico.rehwaldt
 */
public abstract class ModelTextBinding<V> extends ModelViewBinding<Text, V> {

	protected boolean disableOnNull = false;
	protected boolean viewUpdate = false;
	private boolean focusOnNonNull = false;

	public ModelTextBinding(EObject model, Text control) {
		super(model, control);
	}

	@Override
	public void setViewValue(V value) {
		String str = toString(value);

		control.setText(str);
		
		if (control.isFocusControl()) {
			control.setSelection(str.length());
		}
	}

	protected void updateViewState(V modelValue) {
		if (disableOnNull) {
			control.setEnabled(modelValue != null);
		}
		
		if (focusOnNonNull && modelValue != null) {
			if (!control.isFocusControl()) {
				control.setFocus();
			}
		}
	}
	
	/**
	 * Returns the current view value or throws an exception if 
	 * it cannot be parsed
	 * 
	 * @return the view value
	 * 
	 * @throws IllegalArgumentException if the view value cannot be obtained
	 */
	@Override
	public V getViewValue() throws IllegalArgumentException {
		return fromString(control.getText());
	}

	/**
	 * Converts a model value to its string representation
	 * 
	 * @param value
	 * @return
	 */
	protected abstract String toString(V value);
	
	/**
	 * Converts a string representation of a model value to 
	 * its actual value
	 * 
	 * @param str
	 * @return
	 */
	protected abstract V fromString(String str);

	@Override
	protected void establishModelViewBinding() {
		ensureChangeSupportAdded();

		control.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				viewUpdate = true;
				
				try {
					V modelValue = getModelValue();
					V viewValue = null;
					
					try {
						viewValue = getViewValue();
					} catch (IllegalArgumentException e) {
						; // expected
					}

					log(String.format("\t%s\t%s\t%s", "text", "M->V", "?"));
					
					if (isChangeWithNullChecks(viewValue, modelValue)) {
						log(String.format("\t%s\t%s\t%s -> %s", "text", "M->V", viewValue, modelValue));
						setViewValue(modelValue);
					}
					
					updateViewState(modelValue);
				} finally {
					viewUpdate = false;
				}
			}
		});
	}
	
	/**
	 * Ensure that change support is added for the given binding
	 */
	protected abstract void ensureChangeSupportAdded();
	
	@Override
	protected void establishViewModelBinding() {
		control.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				if (viewUpdate) {
					return;
				}
				
				try {
					V viewValue = fromString(control.getText());
					V modelValue = getModelValue();
					
					if (isChangeWithNullChecks(modelValue, viewValue)) {
						log(String.format("\t%s\t%s\t%s -> %s", "text", "V->M", modelValue, viewValue));
						setModelValue(viewValue);
					}
				} catch (IllegalArgumentException e) {
					; // expected
				}
			}
		});
	}

	public void setDisableOnNull(boolean disableOnNull) {
		this.disableOnNull = disableOnNull;
	}

	public void setFocusOnNonNull(boolean focusOnNonNull) {
		this.focusOnNonNull = focusOnNonNull;
	}
}
