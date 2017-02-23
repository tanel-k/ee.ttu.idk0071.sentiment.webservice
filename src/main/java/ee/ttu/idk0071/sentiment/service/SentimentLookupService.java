package ee.ttu.idk0071.sentiment.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.dao.BusinessRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentLookupRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentSnapshotRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentTypeRepository;
import ee.ttu.idk0071.sentiment.model.Business;
import ee.ttu.idk0071.sentiment.model.BusinessType;
import ee.ttu.idk0071.sentiment.model.Country;
import ee.ttu.idk0071.sentiment.model.SentimentLookup;
import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;

@Service
public class SentimentLookupService {
	@Autowired
	private SentimentLookupRepository sentimentLookupRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private SentimentSnapshotRepository sentimentSnapshotRepository;
	@Autowired
	private SentimentTypeRepository sentimentTypeRepository;

	public SentimentLookup getLookup(Long id) {
		return sentimentLookupRepository.findOne(id);
	}

	public SentimentLookup beginLookup(Country country, BusinessType businessType, String businessName) {
		Business business = businessRepository.findByCountryAndBusinessTypeAndBusinessName(country, businessType, businessName);
		
		if (business == null) {
			business = new Business();
			business.setBusinessType(businessType);
			business.setCountry(country);
			business.setBusinessName(businessName);
			businessRepository.save(business);
		}
		
		SentimentLookup sentimentLookup = new SentimentLookup();
		sentimentLookup.setBusiness(business);
		sentimentLookup.setDate(new Date());
		sentimentLookupRepository.save(sentimentLookup);
		
		// TODO: lookup logic
		// TODO: queue logic
		for (int i = 1; i < 101; i++) {
			SentimentSnapshot fakeSnapShot = new SentimentSnapshot();
			fakeSnapShot.setRank(Long.valueOf(i));
			fakeSnapShot.setTitle("Dummy title #" + i);
			fakeSnapShot.setUrl("example.com");
			fakeSnapShot.setTrustLevel(0.06F);
			fakeSnapShot.setSentimentLookup(sentimentLookup);
			fakeSnapShot.setSentimentType(sentimentTypeRepository.findOne("NEU"));
			sentimentSnapshotRepository.save(fakeSnapShot);
		}
		
		return sentimentLookup;
	}
}
