package ua.com.yaremko.system.core.entity;

import java.util.Comparator;
import java.util.List;

public class SubjectDTO implements Comparable<SubjectDTO> {

	private String name;
	private String faculty;
	private String type;
	private String term;
	private String creditsNum;
	private String weekHours;
	private String totalHours;

	private List<String> subjectResearchLines;
	private List<String> preSubjects;
	private List<String> postSubjects;

	public static final Comparator<SubjectDTO> SORT_BY_NAME = new SortedByName();

	public SubjectDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getWeekHours() {
		return weekHours;
	}

	public void setWeekHours(String weekHours) {
		this.weekHours = weekHours;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public List<String> getSubjectResearchLines() {
		return subjectResearchLines;
	}

	public void setSubjectResearchLines(List<String> subjectResearchLines) {
		this.subjectResearchLines = subjectResearchLines;
	}

	public List<String> getPreSubjects() {
		return preSubjects;
	}

	public void setPreSubjects(List<String> preSubjects) {
		this.preSubjects = preSubjects;
	}

	public List<String> getPostSubjects() {
		return postSubjects;
	}

	public void setPostSubjects(List<String> postSubjects) {
		this.postSubjects = postSubjects;
	}

	@Override
	public String toString() {
		return "SubjectDTO [name=" + name + ", faculty=" + faculty + ", type=" + type + ", term=" + term
				+ ", creditsNum=" + creditsNum + ", weekHours=" + weekHours + ", totalHours=" + totalHours + "]";
	}

	@Override
	public int compareTo(SubjectDTO subjectDTO) {

		return name.compareTo(subjectDTO.getName());
	}

	private static class SortedByName implements Comparator<SubjectDTO> {

		@Override
		public int compare(SubjectDTO o1, SubjectDTO o2) {
			return o1.compareTo(o2);
		}

	}

}
