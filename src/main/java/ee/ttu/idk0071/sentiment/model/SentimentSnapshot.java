package ee.ttu.idk0071.sentiment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SentimentSnapshot {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long rank;
	private String url;
	private String title;
	private Float trustLevel;

	@ManyToOne
	private SentimentLookup sentimentLookup;
	@ManyToOne
	private SentimentType sentimentType;
	@ManyToOne
	private SentimentLookupDomain sentimentLookupDomain;

	public SentimentSnapshot() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getTrustLevel() {
		return trustLevel;
	}

	public void setTrustLevel(Float trustLevel) {
		this.trustLevel = trustLevel;
	}

	public SentimentLookup getSentimentLookup() {
		return sentimentLookup;
	}

	public void setSentimentLookup(SentimentLookup sentimentLookup) {
		this.sentimentLookup = sentimentLookup;
	}

	public SentimentType getSentimentType() {
		return sentimentType;
	}

	public void setSentimentType(SentimentType sentimentType) {
		this.sentimentType = sentimentType;
	}

	public SentimentLookupDomain getSentimentLookupDomain() {
		return sentimentLookupDomain;
	}

	public void setSentimentLookupDomain(SentimentLookupDomain sentimentLookupDomain) {
		this.sentimentLookupDomain = sentimentLookupDomain;
	}
}
