package ee.ttu.idk0071.sentiment.service.objects;

import java.util.Date;

import ee.ttu.idk0071.sentiment.model.DomainLookup;

public class DomainLookupResult implements Comparable<DomainLookupResult> {
	private Float negativityPercentage;
	private Float neutralityPercentage;
	private Float positivityPercentage;
	private Date date;

	public Float getNegativityPercentage() {
		return negativityPercentage;
	}

	public void setNegativityPercentage(Float negativityPercentage) {
		this.negativityPercentage = negativityPercentage;
	}

	public Float getNeutralityPercentage() {
		return neutralityPercentage;
	}

	public void setNeutralityPercentage(Float neutralityPercentage) {
		this.neutralityPercentage = neutralityPercentage;
	}

	public Float getPositivityPercentage() {
		return positivityPercentage;
	}

	public void setPositivityPercentage(Float positivityPercentage) {
		this.positivityPercentage = positivityPercentage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(DomainLookupResult other) {
		if (other.getDate() == null) {
			if (this.getDate() == null) {
				return 0;
			} else {
				return -1;
			}
		}
		
		if (this.getDate() == null) {
			return 1;
		}
		
		return this.getDate().compareTo(other.getDate());
	}

	public static DomainLookupResult forDomainLookup(DomainLookup domainLookup) {
		DomainLookupResult snapshot = new DomainLookupResult();
		
		snapshot.setDate(domainLookup.getLookup().getDate());
		snapshot.setNegativityPercentage(domainLookup.getNegativityPercentage());
		snapshot.setNeutralityPercentage(domainLookup.getNeutralityPercentage());
		snapshot.setPositivityPercentage(domainLookup.getPositivityPercentage());
		
		return snapshot;
	}
}
