package ua.com.yaremko.system.core;

import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
	private OWLReasoner reasoner;

	private ShortFormProvider shortFormProvider;

	private DLQueryPrinter dlQueryPrinter;

	public DLQuery(OWLEditorKit owlEditorKit) {

		reasonerManager = owlEditorKit.getOWLModelManager().getOWLReasonerManager();

		ReasonerUtilities.warnUserIfReasonerIsNotConfigured(owlEditorKit.getOWLWorkspace(), reasonerManager);

		reasoner = reasonerManager.getCurrentReasoner();

		shortFormProvider = new SimpleShortFormProvider();
		// Create the DLQueryPrinter helper class. This will manage the
		// parsing
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

	public Set<OWLClass> getSuperClassesSet(String classExpression, boolean direct) {
		return dlQueryPrinter.printSuperClassesSet(classExpression, direct);
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

	public Set<OWLClass> getSubClassesSet(String classExpression, boolean direct) {
		return dlQueryPrinter.printSubClassesSet(classExpression, direct);
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

	public Set<OWLClass> getEquivalentClassesSet(String classExpression) {
		return dlQueryPrinter.printEquivalentClassesSet(classExpression);
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

	public Set<OWLNamedIndividual> getInstancesSet(String classExpression, boolean direct) {
		return dlQueryPrinter.printInstancesSet(classExpression, direct);
	}
	/*
	 * == tests for reference equality (whether they are the same object)
		equals() tests for value equality (whether they are logically "equal")
	 */
	public String fromDLQueryParamsToRequest(DLQueryParams dlQueryParams) {
		StringBuilder sb = new StringBuilder();
		String dlQuery = "";
		String subRequest="";
		
		if(dlQueryParams.getSpeciality() == null 
				|| dlQueryParams.getSpeciality().equals(SubjectPropertiesConstants.SPECIALITY_DEF)){
			dlQuery = String.format("Предмет and " + "вивчає some %s",
					dlQueryParams.getScienceBranch());
		}else if (dlQueryParams.getResearchLine() == null
				|| dlQueryParams.getResearchLine().equals(SubjectPropertiesConstants.RESEARCH_LINE_DEF)) {
			dlQuery = String.format("Предмет and " + "вивчає some %s",
					dlQueryParams.getSpeciality());

		} else {
			dlQuery = String.format("Предмет and " + "вивчає some %s",
					dlQueryParams.getResearchLine());
		}
		sb.append(dlQuery);
		
		if(dlQueryParams.getSubjectType() != null && !dlQueryParams.getSubjectType().equals(SubjectPropertiesConstants.SUBJECT_TYPE_DEF)){
			subRequest = String.format(" and типПредмету some %s", dlQueryParams.getSubjectType());
			sb.append(subRequest);
		}
		if(dlQueryParams.getTerm() != null && !dlQueryParams.getTerm().equals(SubjectPropertiesConstants.TERM_DEF)){
			subRequest = String.format(" and семестр value \"%s\"^^xsd:string", dlQueryParams.getTerm());
			sb.append(subRequest);
		}
		if(dlQueryParams.getCreditsNum() != null && !dlQueryParams.getCreditsNum().equals(SubjectPropertiesConstants.CREDINS_NUM_DEF)){
			subRequest = String.format(" and кількістьКредитів value \"%s\"^^xsd:double", dlQueryParams.getCreditsNum());
			sb.append(subRequest);
		}
		
		System.out.println("FINAL DLQUERY REAQUEST: " + sb.toString());
		return sb.toString();
	}
	
/*
	public String fromDLQueryParamsToRequest(DLQueryParams dlQueryParams) {
		String dlQuery = "";

		if (dlQueryParams.getResearchLine() == null
				|| dlQueryParams.getResearchLine().equals("--- виберіть напрям дослідження(не обов'язково) ---")) {
			dlQuery = String.format("Предмет and " + "вивчає some %s" + " and типПредмету some %s"
					+ " and семестр value \"%s\"^^xsd:string" + " and кількістьКредитів value \"%s\"^^xsd:double",
					dlQueryParams.getSpeciality(), dlQueryParams.getSubjectType(), dlQueryParams.getTerm(),
					dlQueryParams.getCreditsNum());

		} else {
			dlQuery = String.format("Предмет and " + "вивчає some %s" + " and типПредмету some %s"
					+ " and семестр value \"%s\"^^xsd:string" + " and кількістьКредитів value \"%s\"^^xsd:double",
					dlQueryParams.getResearchLine(), dlQueryParams.getSubjectType(), dlQueryParams.getTerm(),
					dlQueryParams.getCreditsNum());
		}

		return dlQuery;
	}
	*/

}
