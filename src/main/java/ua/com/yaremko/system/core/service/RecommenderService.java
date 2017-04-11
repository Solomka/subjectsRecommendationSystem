package ua.com.yaremko.system.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import ua.com.yaremko.system.core.SubjectPropertiesConstants;
import ua.com.yaremko.system.core.converter.DLQueryParamsRequestStringConverter;
import ua.com.yaremko.system.core.converter.OWLClassSubjectDTOConverter;
import ua.com.yaremko.system.core.dl.DLQuery;
import ua.com.yaremko.system.core.entity.DLQueryParams;
import ua.com.yaremko.system.core.entity.QueryResult;
import ua.com.yaremko.system.core.entity.Relaxations;
import ua.com.yaremko.system.core.entity.SubjectDTO;

/**
 * responsible for recommenadation retrival process
 * 
 * @author Solomka
 *
 */
public class RecommenderService {

	private OWLModelManager owlmodelManager;
	private OWLEditorKit owlEditorKit;

	private OWLDataFactory owlDataFactor;
	private DLQuery dlQuery;
	private OWLOntology owlOntology;

	public RecommenderService(OWLModelManager modelManager, OWLEditorKit owlEditorKit) {
		this.owlmodelManager = modelManager;
		this.owlEditorKit = owlEditorKit;

		this.owlDataFactor = owlmodelManager.getOWLDataFactory();
		this.dlQuery = new DLQuery(this.owlEditorKit);
		this.owlOntology = owlmodelManager.getOWLReasonerManager().getCurrentReasoner().getRootOntology();
	}

	public QueryResult processQuery(DLQueryParams dlQueryParams) {
		QueryResult queryResult = new QueryResult();

		String dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);
		Set<OWLClass> recommSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);

		if (recommSubjects.size() == 1 && recommSubjects
				.contains(owlDataFactor.getOWLClass(IRI.create(SubjectPropertiesConstants.NOTHING_CLASS_IRI)))) {
			queryResult = processRestrictionsRelaxation(dlQueryParams, recommSubjects);
		} else {
			queryResult.setOwlClasses(recommSubjects);
		}
		return queryResult;
	}

	/**
	 * process Subjects Query with Restrictions Relaxation
	 * 
	 * @param dlQueryParams
	 * @param recommSubjects
	 * @return
	 */
	private QueryResult processRestrictionsRelaxation(DLQueryParams dlQueryParams, Set<OWLClass> recommSubjects) {

		QueryResult queryResult = new QueryResult();

		DLQueryParams dlQueryParamsParameters = dlQueryParams;
		String dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);

		Set<OWLClass> recommenderSubjects = recommSubjects;
		List<Relaxations> appliedRelax = new ArrayList<>();

		String subjectType = null;

		while (recommenderSubjects.size() == 1 && recommenderSubjects
				.contains(owlDataFactor.getOWLClass(IRI.create(SubjectPropertiesConstants.NOTHING_CLASS_IRI)))) {

			if (dlQueryParamsParameters.getSubjectType() != null) {
				subjectType = dlQueryParamsParameters.getSubjectType();
				dlQueryParamsParameters.setSubjectType(null);
				appliedRelax.add(Relaxations.SUBJECT_TYPE);

			} else if (dlQueryParamsParameters.getResearchLine() != null
					&& appliedRelax.contains(Relaxations.SUBJECT_TYPE)) {
				dlQueryParamsParameters.setSubjectType(subjectType);
				appliedRelax.remove(Relaxations.SUBJECT_TYPE);

				dlQueryParamsParameters.setResearchLine(null);
				appliedRelax.add(Relaxations.RESEARCH_LINE);

			} else if (dlQueryParamsParameters.getResearchLine() != null
					&& !appliedRelax.contains(Relaxations.SUBJECT_TYPE)) {
				dlQueryParamsParameters.setResearchLine(null);
				appliedRelax.add(Relaxations.RESEARCH_LINE);

			} else if (dlQueryParamsParameters.getSpeciality() != null
					&& appliedRelax.contains(Relaxations.SUBJECT_TYPE)
					&& appliedRelax.contains(Relaxations.RESEARCH_LINE)) {
				dlQueryParamsParameters.setSpeciality(null);
				appliedRelax.add(Relaxations.SPECIALITY);

			} else if (dlQueryParamsParameters.getTerm() != null && appliedRelax.contains(Relaxations.SUBJECT_TYPE)
					&& appliedRelax.contains(Relaxations.RESEARCH_LINE)
					&& appliedRelax.contains(Relaxations.SPECIALITY)) {
				dlQueryParamsParameters.setTerm(null);
				appliedRelax.add(Relaxations.TERM);

			} else if (dlQueryParamsParameters.getCreditsNum() != null
					&& appliedRelax.contains(Relaxations.SUBJECT_TYPE)
					&& appliedRelax.contains(Relaxations.RESEARCH_LINE) && appliedRelax.contains(Relaxations.SPECIALITY)
					&& appliedRelax.contains(Relaxations.TERM)) {
				dlQueryParamsParameters.setCreditsNum(null);
				appliedRelax.add(Relaxations.TERM);

			} else {
				JOptionPane.showMessageDialog(null, "Немає відповідних рекомендацій! \n Змініть критерії пошуку!",
						"Увага!", JOptionPane.ERROR_MESSAGE);
				break;
			}

			dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);
			System.out.println("Relaxed query: " + dlQueryRequest);
			recommenderSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);
		}

		queryResult.setOwlClasses(recommenderSubjects);
		queryResult.setAppliedRelax(appliedRelax);

		return queryResult;
	}

	/**
	 * Get sorted by name SubjectDTOs list for ShowSubjectsPanel
	 * 
	 * @param recommSubjects
	 * @return
	 */
	public List<SubjectDTO> generateSubjectsRecommendation(Set<OWLClass> recommSubjects) {

		List<SubjectDTO> recommendedSubjects = new ArrayList<>();
		OWLClassSubjectDTOConverter owlClassSubjectDTOConverter;
		SubjectDTO subject = new SubjectDTO();

		// fill List <SubjectDTO> recommendedSubjects
		for (OWLClass c : recommSubjects) {
			owlClassSubjectDTOConverter = new OWLClassSubjectDTOConverter(owlOntology);
			subject = owlClassSubjectDTOConverter.fromOWLClassToSubjectDTO(c);
			recommendedSubjects.add(subject);
		}
		Collections.sort(recommendedSubjects);
		// Collections.sort(recommendedSubjects, SubjectDTO.SORT_BY_NAME);

		return recommendedSubjects;

	}

}
