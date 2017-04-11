package ua.com.yaremko.system.core.converter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import ua.com.yaremko.system.core.RestrictionVisitor;
import ua.com.yaremko.system.core.SubjectPropertiesConstants;
import ua.com.yaremko.system.core.entity.SubjectDTO;

/**
 * convert OWLClass to SubjectDTO
 * 
 * @author Solomka
 *
 */
public final class OWLClassSubjectDTOConverter {

	private OWLOntology currectOntology;
	private RestrictionVisitor restrictionVisitor;
	private ShortFormProvider shortFormProvider;

	public OWLClassSubjectDTOConverter(OWLOntology currectOntology) {
		this.currectOntology = currectOntology;
		restrictionVisitor = new RestrictionVisitor(Collections.singleton(currectOntology));
		shortFormProvider = new SimpleShortFormProvider();
	}

	public SubjectDTO fromOWLClassToSubjectDTO(OWLClass c) {

		SubjectDTO subject = new SubjectDTO();

		// get short subject name
		String shortSubjectName = shortFormProvider.getShortForm(c);

		for (OWLSubClassOfAxiom ax : currectOntology.getSubClassAxiomsForSubClass(c)) {
			ax.getSuperClass().accept(restrictionVisitor);
		}

		// print Object Properies
		Map<String, String> objectProperties = restrictionVisitor.getObjectProps();
		for (String key : objectProperties.keySet()) {
			if (key.equals(SubjectPropertiesConstants.BELONGS_TO_FACULTY)) {
				subject.setFaculty(objectProperties.get(key));
			} else if (key.equals(SubjectPropertiesConstants.SUBJECT_TYPE)) {
				subject.setType(objectProperties.get(key));
			}

		}

		// print Data Properties
		Map<String, String> dataProperties = restrictionVisitor.getDataProps();
		for (String key : dataProperties.keySet()) {
			if (key.equals(SubjectPropertiesConstants.TERM)) {
				subject.setTerm(dataProperties.get(key));
			} else if (key.equals(SubjectPropertiesConstants.CREDITS_NUM)) {
				subject.setCreditsNum(dataProperties.get(key));
			} else if (key.equals(SubjectPropertiesConstants.WEEK_HOURS_NUM)) {
				subject.setWeekHours(dataProperties.get(key));
			} else if (key.equals(SubjectPropertiesConstants.TOTAL_HOURS_NUM)) {
				subject.setTotalHours(dataProperties.get(key));
			}
		}

		// print subjectResearchLines
		List<String> subjectResearchLines = restrictionVisitor.getSubjectResearchLines();
		subject.setSubjectResearchLines(subjectResearchLines);

		// print suject's preSubjects
		List<String> preSubjects = restrictionVisitor.getPreSubjects();
		subject.setPreSubjects(preSubjects);

		// print subject's postSubjects
		List<String> postSubjects = restrictionVisitor.getPostSubjects();
		subject.setPostSubjects(postSubjects);

		// fill SubjectDTO
		subject.setName(shortSubjectName);

		return subject;
	}
}
