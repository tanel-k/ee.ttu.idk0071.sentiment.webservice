package ee.ttu.idk0071.sentiment.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.dao.BusinessRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentLookupDomainRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentLookupRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentSnapshotRepository;
import ee.ttu.idk0071.sentiment.dao.SentimentTypeRepository;
import ee.ttu.idk0071.sentiment.lib.analysis.SentimentAnalyzer;
import ee.ttu.idk0071.sentiment.lib.analysis.impl.BasicSentimentAnalyzer;
import ee.ttu.idk0071.sentiment.lib.analysis.objects.PageSentiment;
import ee.ttu.idk0071.sentiment.lib.scraping.SearchEngineScraper;
import ee.ttu.idk0071.sentiment.lib.scraping.impl.GoogleScraper;
import ee.ttu.idk0071.sentiment.lib.scraping.objects.SearchEngineQuery;
import ee.ttu.idk0071.sentiment.lib.scraping.objects.SearchEngineResult;
import ee.ttu.idk0071.sentiment.model.Business;
import ee.ttu.idk0071.sentiment.model.BusinessType;
import ee.ttu.idk0071.sentiment.model.Country;
import ee.ttu.idk0071.sentiment.model.SentimentLookup;
import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;
import ee.ttu.idk0071.sentiment.model.SentimentType;

@Service
public class SentimentLookupService {
	@Autowired
	private SentimentLookupDomainRepository sentimentLookupDomainRepository;
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
		
		SearchEngineScraper scraper = new GoogleScraper();
		String queryString = businessName + " AND  " + businessType.getName() + " AND " + country.getCode();
		SearchEngineQuery query = new SearchEngineQuery(queryString, 10);
		List<SearchEngineResult> searchLinks = scraper.search(query);
		
		int neuCnt = 0, posCnt = 0, negCnt = 0;
		SentimentAnalyzer analyzer = new BasicSentimentAnalyzer(500);
		
		for (SearchEngineResult searchLink : searchLinks) {
			try {
				PageSentiment sentiment = analyzer.analyzePage(searchLink.getUrl());
				
				SentimentSnapshot snapshot = new SentimentSnapshot();
				snapshot.setRank(searchLink.getRank());
				snapshot.setTitle(searchLink.getTitle());
				snapshot.setUrl(searchLink.getUrl());
				snapshot.setTrustLevel(sentiment.getTrustLevel());
				snapshot.setSentimentLookup(sentimentLookup);
				
				SentimentType pageSentimentType = null;
				switch (sentiment.getSentimentType()) {
					case NEUTRAL:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEUTRAL);
						neuCnt++;
						break;
					case POSITIVE:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_POSITIVE);
						posCnt++;
						break;
					case NEGATIVE:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEGATIVE);
						negCnt++;
						break;
					default:
						break;
				}
				
				snapshot.setSentimentType(pageSentimentType);
				snapshot.setSentimentLookupDomain(sentimentLookupDomainRepository.findByName("Google"));
				sentimentSnapshotRepository.save(snapshot);
			} catch (Throwable t) {
				continue;
			}
		}
		
		SentimentType lookupSentimentType = null;
		if (neuCnt >= posCnt) {
			if (neuCnt >= negCnt) {
				lookupSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEUTRAL);
			} else {
				lookupSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEGATIVE);
			}
		} else {
			if (posCnt >= negCnt) {
				lookupSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_POSITIVE);
			} else {
				lookupSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEGATIVE);
			}
		}
		
		sentimentLookup.setSentimentType(lookupSentimentType);
		sentimentLookupRepository.save(sentimentLookup);
		return sentimentLookup;
	}
}