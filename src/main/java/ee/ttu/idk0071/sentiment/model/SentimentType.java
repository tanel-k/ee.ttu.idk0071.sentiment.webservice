package ee.ttu.idk0071.sentiment.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SentimentType {
	public static final String TYPE_CODE_POSITIVE = "POS";
	public static final String TYPE_CODE_NEUTRAL = "NEU";
	public static final String TYPE_CODE_NEGATIVE = "NEG";
	
	@Id
	private String code;
	private String name;

	public SentimentType() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
