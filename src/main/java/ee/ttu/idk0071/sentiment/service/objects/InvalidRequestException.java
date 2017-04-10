package ee.ttu.idk0071.sentiment.service.objects;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = -3315130441804107436L;

	public InvalidRequestException(String msg) {
		super(msg);
	}

	public InvalidRequestException(Throwable t) {
		super(t);
	}

	public InvalidRequestException(String msg, Throwable t) {
		super(msg, t);
	}
}
