package ee.ttu.idk0071.sentiment.controller.objects;

public class SentimentLookupRequest {
	private String countryCode;
	private Integer businessTypeId;
	private String businessName;

	public SentimentLookupRequest() {
		
	}

	public String getCountryId() {
		return countryCode;
	}

	public void setCountryId(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessType(Integer businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

}
