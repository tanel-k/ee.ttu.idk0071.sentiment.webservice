package ee.ttu.idk0071.sentiment.controller.messages;

import java.util.List;

public class LookupRequest {
	private String entityName;

	private List<Integer> domainIds;

	public LookupRequest() {
		
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
