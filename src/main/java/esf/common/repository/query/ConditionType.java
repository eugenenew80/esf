package esf.common.repository.query;

public enum ConditionType {
	EQUALS("="),
	NOT_EQUALS("<>"),
	LIKE("like"),
	GT(">"),
	LT("<"),
	GTE(">="),
	LTE("<=");

	
	private ConditionType(String operator) {
		this.operator = operator;
	}


	public String toString() {
		return this.operator;
	}
	
	private String operator;
}
