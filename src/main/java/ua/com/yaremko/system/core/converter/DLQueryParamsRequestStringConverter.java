package ua.com.yaremko.system.core.converter;

import ua.com.yaremko.system.core.SubjectPropertiesConstants;
import ua.com.yaremko.system.core.entity.DLQueryParams;

/**
 * Convert DLQueryParams to DLQuery Request String
 * 
 * @author Solomka
 *
 */
public final class DLQueryParamsRequestStringConverter {

	private DLQueryParamsRequestStringConverter() {
		throw new RuntimeException();
	}

	/*
	 * == tests for reference equality (whether they are the same object)
	 * equals() tests for value equality (whether they are logically "equal")
	 */
	public static String fromDLQueryParamsToRequest(DLQueryParams dlQueryParams) {
		StringBuilder sb = new StringBuilder();
		String dlQuery = "";
		String subRequest = "";

		if (dlQueryParams.getSpeciality() == null
				|| dlQueryParams.getSpeciality().equals(SubjectPropertiesConstants.SPECIALITY_DEF)) {
			dlQuery = String.format("Предмет and " + "вивчає some %s", dlQueryParams.getScienceBranch());
		} else if (dlQueryParams.getResearchLine() == null
				|| dlQueryParams.getResearchLine().equals(SubjectPropertiesConstants.RESEARCH_LINE_DEF)) {
			dlQuery = String.format("Предмет and " + "вивчає some %s", dlQueryParams.getSpeciality());

		} else {
			dlQuery = String.format("Предмет and " + "вивчає some %s", dlQueryParams.getResearchLine());
		}
		sb.append(dlQuery);

		if (dlQueryParams.getSubjectType() != null
				&& !dlQueryParams.getSubjectType().equals(SubjectPropertiesConstants.SUBJECT_TYPE_DEF)) {
			subRequest = String.format(" and типПредмету some %s", dlQueryParams.getSubjectType());
			sb.append(subRequest);
		}
		if (dlQueryParams.getTerm() != null && !dlQueryParams.getTerm().equals(SubjectPropertiesConstants.TERM_DEF)) {
			subRequest = String.format(" and семестр value \"%s\"^^xsd:string", dlQueryParams.getTerm());
			sb.append(subRequest);
		}
		if (dlQueryParams.getCreditsNum() != null
				&& !dlQueryParams.getCreditsNum().equals(SubjectPropertiesConstants.CREDINS_NUM_DEF)) {
			subRequest = String.format(" and кількістьКредитів value \"%s\"^^xsd:double",
					dlQueryParams.getCreditsNum());
			sb.append(subRequest);
		}

		System.out.println("FINAL DLQUERY REAQUEST: " + sb.toString());
		
		return sb.toString();
	}

}
