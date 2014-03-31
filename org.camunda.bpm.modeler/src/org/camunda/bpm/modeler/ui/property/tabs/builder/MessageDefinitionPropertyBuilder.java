package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the message property.
 * 
 * @author nico.rehwaldt
 */
public class MessageDefinitionPropertyBuilder extends DefinitionReferencePropertyBuilder<Message> {

	private static final EStructuralFeature MESSAGE_REF_FEATURE = Bpmn2Package.eINSTANCE.getMessageEventDefinition_MessageRef();
	private static final EStructuralFeature MESSAGE_NAME_FEATURE = Bpmn2Package.eINSTANCE.getMessage_Name();

	public MessageDefinitionPropertyBuilder(Composite parent, GFPropertySection section, MessageEventDefinition bo) {
		super(parent, section, bo, "Message", MESSAGE_REF_FEATURE, MESSAGE_NAME_FEATURE, null, Message.class);
	}
	
	@Override
	public void create() {
		// create message ref
		super.create();
		
		// create message definitions table
		createDefinitionsTable();
	}

	private void createDefinitionsTable() {
		new DefinitionsPropertiesBuilder(parent, section, ModelUtil.getDefinitions(bo)).createMessageMappingsTable();
	}
}
