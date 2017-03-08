package ua.com.yaremko.system.menu;

import java.awt.event.ActionEvent;
import java.util.Set;

import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerStatus;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolsMenu3 extends ProtegeOWLAction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ToolsMenu3.class.getClass());

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}

	/*
	 * public void actionPerformed(ActionEvent event) { StringBuilder message =
	 * new StringBuilder(
	 * "This example menu item is under the Tools menu, but displayed in a separate category from the other example menu items.\n"
	 * ); message.append("The active ontology has ");
	 * message.append(getOWLModelManager().getActiveOntology().
	 * getClassesInSignature().size()); message.append(" classes.");
	 * JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString()); }
	 */
	public void actionPerformed(ActionEvent e) {
		OWLClass lastSelectedClass = getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
		if (lastSelectedClass != null) {
			OWLReasonerManager reasonerManager = getOWLModelManager().getOWLReasonerManager();
			ReasonerUtilities.warnUserIfReasonerIsNotConfigured(getOWLWorkspace(), reasonerManager);
			if (reasonerManager.getReasonerStatus() == ReasonerStatus.INITIALIZED) {
				//get selected initialized ready for use reasoner 
				OWLReasoner reasoner = reasonerManager.getCurrentReasoner();
				Set<OWLClass> subClasses = reasoner.getSubClasses(lastSelectedClass, true).getFlattened();
				if (!subClasses.isEmpty()) {
					LOGGER.info(
							"Inferred subclasses of " + getOWLModelManager().getRendering(lastSelectedClass) + " are:");
					for (OWLClass subClass : subClasses) {
						LOGGER.info("\t" + getOWLModelManager().getRendering(subClass));
					}
				} else {
					LOGGER.info(getOWLModelManager().getRendering(lastSelectedClass) + " has no inferred subclasses.");
				}
			}
		}
	}
}
