package ee.ttu.idk0071.sentiment.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SentimentLookupDomain {
	@Id
	private Integer id;
	private String name;

	public SentimentLookupDomain() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
