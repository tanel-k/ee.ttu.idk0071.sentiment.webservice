package ee.ttu.idk0071.sentiment.service.objects;

public class MissingDomainLookupException extends Exception
{
	private static final long serialVersionUID = 3671598666659160944L;
	private Long domainLookupId;

	public Long getDomainLookupId() {
		return domainLookupId;
	}

	public MissingDomainLookupException(Long domainLookupId) {
		this.domainLookupId = domainLookupId;
	}
}
