package ua.com.yaremko.system.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLHasValueRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

/**
 * Visits existential restrictions and collects the properties which are
 * restricted.
 */
public class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {
	
	private static final String STUDY_OBJECT_PROP = "вивчає";
	private static final String HAS_SUBJECT_CONTINUE = "маєПредметПрод";
	private static final String IS_SUBJECT_CONTINUE = "єПредметПрод";

	private final Set<OWLOntology> onts;
	private final Set<OWLClass> processedClasses;
	
	//OWLObjectProperties
	private final Set<OWLObjectPropertyExpression> restrictedProperties;
	//OWLObjectPropertiesValues
	private final Set<OWLClassExpression> restrictedPropertiesValues;
	
	private final Set<OWLDataPropertyExpression> dataProperties;
	private final Set<OWLLiteral> dataPropertiesValues;
	
	//private final Map<OWLObjectPropertyExpression, OWLClassExpression> objectProps;
	//private final Map<OWLDataPropertyExpression, OWLLiteral> dataProps;
	
	private final Map<String, String> objectProps;
	private final Map<String, String> dataProps;
	
	private final List<String> subjectResearchLines;
	private final List<String> preSubjects;
	private final List<String> postSubjects;
	
	private final ShortFormProvider shortFormProvider;
		

	public RestrictionVisitor(Set<OWLOntology> onts) {
		this.onts = onts;		
		this.processedClasses = new HashSet<OWLClass>();		
		this.restrictedProperties = new HashSet<OWLObjectPropertyExpression>();		
		this.restrictedPropertiesValues = new HashSet<OWLClassExpression>();
		this.dataProperties = new HashSet<OWLDataPropertyExpression>();	
		this.dataPropertiesValues = new HashSet<>();
		
		this.objectProps = new HashMap<>();
		this.dataProps = new HashMap<>();
		this.subjectResearchLines = new ArrayList<>();
		preSubjects = new ArrayList<>();
		postSubjects = new ArrayList<>();
		
		this.shortFormProvider = new SimpleShortFormProvider();
	}
	
	public Map<String, String> getObjectProps(){
		return this.objectProps;
	}
	
	public Map<String, String> getDataProps(){
		return this.dataProps;
	}
	
	public List<String> getSubjectResearchLines(){
		return this.subjectResearchLines;
	}
	
	public List<String> getPreSubjects(){
		return this.preSubjects;
	}
	
	public List<String> getPostSubjects(){
		return this.postSubjects;
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

	// This method gets called when a class expression is an existential
			// (someValuesFrom) restriction and it asks us to visit it
	@Override
	public void visit(OWLObjectSomeValuesFrom desc) {
		
		final String objectProperty = shortFormProvider.getShortForm((OWLEntity) desc.getProperty());
		final String objectPropertyValue = shortFormProvider.getShortForm((OWLEntity) desc.getFiller());
		System.out.println("OBJECT PROPERTY: " + objectProperty);
		if(objectProperty.equals(STUDY_OBJECT_PROP)){
			
			subjectResearchLines.add(objectPropertyValue);			
		}
		else if (objectProperty.equals(IS_SUBJECT_CONTINUE)){
			
			preSubjects.add(objectPropertyValue);
			
		}else if(objectProperty.equals(HAS_SUBJECT_CONTINUE)){
			postSubjects.add(objectPropertyValue);			
		}
		else{
		
			restrictedProperties.add(desc.getProperty());
			restrictedPropertiesValues.add(desc.getFiller());
			
			objectProps.put(objectProperty, objectPropertyValue);
					
		}
		}
	
	@Override
	public void visit(OWLDataHasValue desc){
		dataProperties.add(desc.getProperty());
		dataPropertiesValues.add(desc.getFiller());		
		
		dataProps.put(shortFormProvider.getShortForm((OWLEntity) desc.getProperty()),desc.getFiller().getLiteral());
	}

	/*
	 * public void visit(OWLDataHasValue hasValue) { // This method gets called
	 * when a class expression is an existential // (someValuesFrom) restriction
	 * and it asks us to visit it
	 * 
	 * //restrictedProperties.add(desc.getProperty()); hasValue.getProperty(); }
	 */
}