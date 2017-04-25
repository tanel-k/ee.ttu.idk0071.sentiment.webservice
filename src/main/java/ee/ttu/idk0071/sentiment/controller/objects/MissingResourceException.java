package ee.ttu.idk0071.sentiment.controller.objects;

public class MissingResourceException extends Exception {
	private static final long serialVersionUID = -7889749976924675440L;

	public MissingResourceException() {
		
	}

	public MissingResourceException(String message) {
		super(message);
	}
}
