package ee.ttu.idk0071.sentiment.controller.objects;

import java.util.List;

public class SentimentLookupRequest {
	private String entityName;

	private List<Integer> domainIds;

	public SentimentLookupRequest() {
		
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<Integer> getDomainIds() {
		return domainIds;
	}

	public void setDomainIds(List<Integer> domainIds) {
		this.domainIds = domainIds;
	}
}
