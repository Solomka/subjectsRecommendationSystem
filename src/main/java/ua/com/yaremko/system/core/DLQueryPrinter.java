package ua.com.yaremko.system.core;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * Return DLQUery result as a String array
 * 
 * @author Solomka
 *
 */
class DLQueryPrinter {

	private final DLQueryEngine dlQueryEngine;
	private final ShortFormProvider shortFormProvider;

	/**
	 * @param engine
	 *            the engine
	 * @param shortFormProvider
	 *            the short form provider
	 */
	public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
		this.shortFormProvider = shortFormProvider;
		dlQueryEngine = engine;
	}

	/**
	 * print SuperClasses
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */

	public String[] printSuperClasses(String classExpression, boolean direct) {
		Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(classExpression, direct);

		String[] superClassesResult = new String[superClasses.size()];

		int i = 0;
		for (OWLClass superClass : superClasses) {
			superClassesResult[i] = shortFormProvider.getShortForm(superClass);
			System.out.println("ShortForm: " + superClassesResult[i]);
			i++;
		}

		return superClassesResult;

	}

	/**
	 * print Subclasses
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */
	public String[] printSubClasses(String classExpression, boolean direct) {
		Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression, direct);

		System.out.println("Size: " + subClasses.size());

		String[] subClassesResult = new String[subClasses.size()];

		int i = 0;
		for (OWLClass subClass : subClasses) {
			subClassesResult[i] = shortFormProvider.getShortForm(subClass);
			System.out.println("ShortForm: " + subClassesResult[i]);
			i++;
		}

		return subClassesResult;
	}

	/**
	 * print equivalent classes
	 * 
	 * @param classExpression
	 * @return
	 */
	public String[] printEquivalentClasses(String classExpression) {
		Set<OWLClass> equivalentClasses = dlQueryEngine.getEquivalentClasses(classExpression);

		String[] equivalentClassesResult = new String[equivalentClasses.size()];

		int i = 0;
		for (OWLClass equivalentClass : equivalentClasses) {
			equivalentClassesResult[i] = shortFormProvider.getShortForm(equivalentClass);
			System.out.println("ShortForm: " + equivalentClassesResult[i]);
			i++;
		}

		return equivalentClassesResult;

	}

	/**
	 * print instances
	 * 
	 * @param classExpression
	 * @param direct
	 * @return
	 */
	public String[] printInstances(String classExpression, boolean direct) {
		Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(classExpression, direct);

		String[] individualsResult = new String[individuals.size()];

		int i = 0;
		for (OWLNamedIndividual individual : individuals) {
			individualsResult[i] = shortFormProvider.getShortForm(individual);
			System.out.println("ShortForm: " + individualsResult[i]);
			i++;
		}

		return individualsResult;

	}

}
