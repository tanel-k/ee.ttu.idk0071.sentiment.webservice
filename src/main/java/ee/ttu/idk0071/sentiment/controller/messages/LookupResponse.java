package ee.ttu.idk0071.sentiment.controller.messages;

import java.util.List;

public class LookupResponse {
	private Long lookupId;
	private List<Long> domainLookupIds;

	public Long getLookupId() {
		return lookupId;
	}

	public void setLookupId(Long lookupId) {
		this.lookupId = lookupId;
	}

	public List<Long> getDomainLookupIds() {
		return domainLookupIds;
	}

	public void setDomainLookupIds(List<Long> domainLookupIds) {
		this.domainLookupIds = domainLookupIds;
	}
}
