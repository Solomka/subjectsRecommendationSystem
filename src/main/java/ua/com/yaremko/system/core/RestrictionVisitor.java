package ua.com.yaremko.system.core;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLHasValueRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

/**
 * Visits existential restrictions and collects the properties which are
 * restricted.
 */
public class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

	private final Set<OWLOntology> onts;
	private final Set<OWLClass> processedClasses;
	
	//OWLObjectProperties
	private final Set<OWLObjectPropertyExpression> restrictedProperties;
	//OWLObjectPropertiesValues
	private final Set<OWLClassExpression> restrictedPropertiesValues;
	
	private final Set<OWLDataPropertyExpression> dataProperties;
	private final Set<OWLLiteral> dataPropertiesValues;
		

	public RestrictionVisitor(Set<OWLOntology> onts) {
		this.onts = onts;		
		this.processedClasses = new HashSet<OWLClass>();		
		this.restrictedProperties = new HashSet<OWLObjectPropertyExpression>();		
		this.restrictedPropertiesValues = new HashSet<OWLClassExpression>();
		this.dataProperties = new HashSet<OWLDataPropertyExpression>();	
		this.dataPropertiesValues = new HashSet<>();
	}

	public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
		return this.restrictedProperties;
	}

	public Set<OWLClassExpression> getRestrictedPropertiesValues() {
		return this.restrictedPropertiesValues;
	}
	
	public Set<OWLDataPropertyExpression> getDataProperties(){
		return this.dataProperties;
	}
	
	public Set<OWLLiteral> getDataPropertiesValues(){
		return this.dataPropertiesValues;
	}

	@Override
	public void visit(OWLClass desc) {
		if (!processedClasses.contains(desc)) {
			// If we are processing inherited restrictions then we
			// recursively visit named supers. Note that we need to keep
			// track of the classes that we have processed so that we don't
			// get caught out by cycles in the taxonomy
			processedClasses.add(desc);
			for (OWLOntology ont : onts) {
				for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)) {
					ax.getSuperClass().accept(this);
				}
			}
		}
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom desc) {
		// This method gets called when a class expression is an existential
		// (someValuesFrom) restriction and it asks us to visit it
		// desc.getFiller();
		restrictedProperties.add(desc.getProperty());
		restrictedPropertiesValues.add(desc.getFiller());
	}
	
	@Override
	public void visit(OWLDataHasValue desc){
		dataProperties.add(desc.getProperty());
		dataPropertiesValues.add(desc.getFiller());		
	}

	/*
	 * public void visit(OWLDataHasValue hasValue) { // This method gets called
	 * when a class expression is an existential // (someValuesFrom) restriction
	 * and it asks us to visit it
	 * 
	 * //restrictedProperties.add(desc.getProperty()); hasValue.getProperty(); }
	 */
}