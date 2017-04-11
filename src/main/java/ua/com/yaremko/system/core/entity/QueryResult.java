package ua.com.yaremko.system.core.entity;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

public class QueryResult {

	private Set<OWLClass> owlClasses;
	private List<Relaxations> appliedRelax;

	public QueryResult() {

	}

	public QueryResult(Set<OWLClass> owlClasses, List<Relaxations> appliedRelax) {

		this.owlClasses = owlClasses;
		this.appliedRelax = appliedRelax;
	}

	public Set<OWLClass> getOwlClasses() {
		return owlClasses;
	}

	public void setOwlClasses(Set<OWLClass> owlClasses) {
		this.owlClasses = owlClasses;
	}

	public List<Relaxations> getAppliedRelax() {
		return appliedRelax;
	}

	public void setAppliedRelax(List<Relaxations> appliedRelax) {
		this.appliedRelax = appliedRelax;
	}

}
