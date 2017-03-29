package ua.com.yaremko.system.core;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

/**
 * 
 * Process dlQuery to the current active Protege ontology
 * 
 * @author Solomka
 *
 */
public class DLQuery {

	private OWLReasonerManager reasonerManager;
	OWLReasoner reasoner;

	private ShortFormProvider shortFormProvider;

	private DLQueryPrinter dlQueryPrinter;

	public DLQuery(OWLEditorKit owlEditorKit) {

		reasonerManager = owlEditorKit.getOWLModelManager().getOWLReasonerManager();

		ReasonerUtilities.warnUserIfReasonerIsNotConfigured(owlEditorKit.getOWLWorkspace(), reasonerManager);

		reasoner = reasonerManager.getCurrentReasoner();

		shortFormProvider = new SimpleShortFormProvider();
		// Create the DLQueryPrinter helper class. This will manage the parsing
		// of input and printing of results
		dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner, shortFormProvider), shortFormProvider);
	}

	/**
	 * get superclasses
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */
	public String[] getSuperClasses(String classExpression, boolean direct) {
		return dlQueryPrinter.printSuperClasses(classExpression, direct);
	}

	/**
	 * get subclasses
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */
	public String[] getSubClasses(String classExpression, boolean direct) {
		return dlQueryPrinter.printSubClasses(classExpression, direct);
	}

	/**
	 * get equivalent classes
	 * 
	 * @param classExpression
	 * @return
	 */
	public String[] getEquivalentClasses(String classExpression) {
		return dlQueryPrinter.printEquivalentClasses(classExpression);
	}

	/**
	 * get instances
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */
	public String[] getInstances(String classExpression, boolean direct) {
		return dlQueryPrinter.printInstances(classExpression, direct);

	}

}
