package ua.com.yaremko.system.core;

public final class SubjectPropertiesConstants {
	
	private SubjectPropertiesConstants(){
		
	}
	
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

}
