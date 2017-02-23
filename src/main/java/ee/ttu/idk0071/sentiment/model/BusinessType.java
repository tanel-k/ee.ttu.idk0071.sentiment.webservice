package ee.ttu.idk0071.sentiment.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BusinessType {
	@Id
	private Integer id;
	private String name;
	
	public BusinessType() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(Integer code) {
		this.id = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
