package ua.com.yaremko.system.core.entity;

public class DLQueryParams {

	private String scienceBranch;
	private String speciality;
	//can be NULL || "--- виберіть напрям дослідження (НЕ ОБОВ'ЯЗКОВО) ---"
	private String researchLine;
	private String subjectType;
	private String term;
	private String creditsNum;

	public DLQueryParams() {

	}

	public DLQueryParams(String scienceBranch, String speciality, String researchLine, String subjectType, String term,
			String creditsNum) {

		this.scienceBranch = scienceBranch;
		this.speciality = speciality;
		this.researchLine = researchLine;
		this.subjectType = subjectType;
		this.term = term;
		this.creditsNum = creditsNum;
	}

	public String getScienceBranch() {
		return scienceBranch;
	}

	public void setScienceBranch(String scienceBranch) {
		this.scienceBranch = scienceBranch;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getResearchLine() {
		return researchLine;
	}

	public void setResearchLine(String researchLine) {
		this.researchLine = researchLine;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getCreditsNum() {
		return creditsNum;
	}

	public void setCreditsNum(String creditsNum) {
		this.creditsNum = creditsNum;
	}
	
	@Override
	public String toString() {
		return "DLQueryParams [scienceBranch=" + scienceBranch + ", speciality=" + speciality + ", researchLine="
				+ researchLine + ", subjectType=" + subjectType + ", term=" + term + ", creditsNum=" + creditsNum + "]";
	}

}
