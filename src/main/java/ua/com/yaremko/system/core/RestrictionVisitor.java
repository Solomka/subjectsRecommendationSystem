package ua.com.yaremko.system.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

/**
 * Visits existentias restrictions and hasVakue restrictions and collects oblect
 * and data props with their values
 */

public class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

	private final Set<OWLOntology> onts;
	private final Set<OWLClass> processedClasses;
	private final ShortFormProvider shortFormProvider;

	private final Map<String, String> objectProps;
	private final Map<String, String> dataProps;

	private final List<String> subjectResearchLines;
	private final List<String> preSubjects;
	private final List<String> postSubjects;

	public RestrictionVisitor(Set<OWLOntology> onts) {
		this.onts = onts;
		this.processedClasses = new HashSet<OWLClass>();
		this.shortFormProvider = new SimpleShortFormProvider();

		this.objectProps = new HashMap<>();
		this.dataProps = new HashMap<>();
		this.subjectResearchLines = new ArrayList<>();
		this.preSubjects = new ArrayList<>();
		this.postSubjects = new ArrayList<>();

	}

	public Map<String, String> getObjectProps() {
		return this.objectProps;
	}

	public Map<String, String> getDataProps() {
		return this.dataProps;
	}

	public List<String> getSubjectResearchLines() {
		return this.subjectResearchLines;
	}

	public List<String> getPreSubjects() {
		return this.preSubjects;
	}

	public List<String> getPostSubjects() {
		return this.postSubjects;
	}

	// If we are processing inherited restrictions then we
	// recursively visit named supers.

	// we need to keep
	// track of the classes that we have processed so that we don't
	// get caught out by cycles in the taxonomy
	@Override
	public void visit(OWLClass desc) {
		if (!processedClasses.contains(desc)) {
			processedClasses.add(desc);
			for (OWLOntology ont : onts) {
				for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)) {
					ax.getSuperClass().accept(this);
				}
			}
		}
	}

	// This method gets called when a class expression is an existential
	// (someValuesFrom) restriction
	// and it asks us to visit it and take its property and property value
	@Override
	public void visit(OWLObjectSomeValuesFrom desc) {

		final String objectProperty = shortFormProvider.getShortForm((OWLEntity) desc.getProperty());
		final String objectPropertyValue = shortFormProvider.getShortForm((OWLEntity) desc.getFiller());
		System.out.println("OBJECT PROPERTY: " + objectProperty);
		if (objectProperty.equals(SubjectPropertiesConstants.STUDY_OBJECT_PROP)) {

			subjectResearchLines.add(objectPropertyValue);
		} else if (objectProperty.equals(SubjectPropertiesConstants.IS_SUBJECT_CONTINUE)) {

			preSubjects.add(objectPropertyValue);

		} else if (objectProperty.equals(SubjectPropertiesConstants.HAS_SUBJECT_CONTINUE)) {
			postSubjects.add(objectPropertyValue);
		} else {

			objectProps.put(objectProperty, objectPropertyValue);

		}
	}

	// This method gets called when a class expression is a hasValue
	// restrictions
	// and it asks us to visit it and take its property and property value
	@Override
	public void visit(OWLDataHasValue desc) {

		dataProps.put(shortFormProvider.getShortForm((OWLEntity) desc.getProperty()), desc.getFiller().getLiteral());
	}

}