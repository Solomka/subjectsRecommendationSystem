package ua.com.yaremko.system.core;

public enum Relaxations {

	SUBJECT_TYPE("Тип предмету"), RESEARCH_LINE("Напрям дослідження"), SPECIALITY("Спеціальність"), TERM("Семестр"), CREDITS_NUM("К-сть крдеитів");

	private String value;

	Relaxations(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
	
	/**
	 * Return relaxation type for a given int value
	 * @param value
	 * @return
	 */

	public static Relaxations forValue(String value) {
		for (final Relaxations relax : Relaxations.values()) {
			if (relax.getValue() == value) {
				return relax;
			}
		}
		return null;

	}
	
	
}
