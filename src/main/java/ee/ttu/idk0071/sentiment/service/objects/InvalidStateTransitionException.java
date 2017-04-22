package ee.ttu.idk0071.sentiment.service.objects;

public class InvalidStateTransitionException extends Exception {
	private static final long serialVersionUID = 4866378527050255593L;

	private Integer fromStateCode;
	private Integer toStateCode;

	public Integer getFromStateCode() {
		return fromStateCode;
	}

	public Integer getToStateCode() {
		return toStateCode;
	}

	public InvalidStateTransitionException(Integer fromStateCode, Integer toStateCode) {
		this.fromStateCode = fromStateCode;
		this.toStateCode = toStateCode;
	}
}
