package ua.com.yaremko.system.core;

public final class SubjectPropertiesConstants {
	
	private SubjectPropertiesConstants(){
		
	}
	
	public static final String PLUGIN_ONTOLOGY_IRI = "http://www.semanticweb.org/solomka/ontologies/subjects";
	public static final String NOTHING_CLASS_IRI = "http://www.w3.org/2002/07/owl#Nothing";
	
	//for RestrictionsVisitor
	public static final String STUDY_OBJECT_PROP = "вивчає";
	public static final String HAS_SUBJECT_CONTINUE = "маєПредметПрод";
	public static final String IS_SUBJECT_CONTINUE = "єПредметПрод";
	
	//ObjectProperties
	public static final String BELONGS_TO_FACULTY = "належитьКафедрі";
	public static final String SUBJECT_TYPE = "типПредмету";
	
	//DataProperties
	public static final String TOTAL_HOURS_NUM = "загальнаКількістьГодин";
	public static final String WEEK_HOURS_NUM = "кількістьГодинНаТиждень";
	public static final String CREDITS_NUM = "кількістьКредитів";
	public static final String TERM = "семестр";
	
	//Valid JComboBox Defaults
	public static final String SCIENCE_BRANCH_DEF = "--- виберіть галузь науки (OБОВ'ЯЗКОВО)---";
	public static final String SPECIALITY_DEF = "--- виберіть спеціальність (НЕ ОБОВ'ЯЗКОВО) ---";
	public static final String RESEARCH_LINE_DEF = "--- виберіть напрям дослідження (НЕ ОБОВ'ЯЗКОВО) ---";
	public static final String SUBJECT_TYPE_DEF = "--- виберіть тип предмету (НЕ ОБОВ'ЯЗКОВО) ---";
	public static final String TERM_DEF = "--- виберіть семестр (НЕ ОБОВ'ЯЗКОВО) ---";
	public static final String CREDINS_NUM_DEF = "--- виберіть к-сть кредитів (НЕ ОБОВ'ЯЗКОВО) ---";

}
