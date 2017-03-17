package ee.ttu.idk0071.sentiment.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.lib.analysis.SentimentAnalyzer;
import ee.ttu.idk0071.sentiment.lib.analysis.impl.BasicSentimentAnalyzer;
import ee.ttu.idk0071.sentiment.lib.analysis.objects.PageSentiment;
import ee.ttu.idk0071.sentiment.lib.scraping.SearchEngineScraper;
import ee.ttu.idk0071.sentiment.lib.scraping.impl.GoogleScraper;
import ee.ttu.idk0071.sentiment.lib.scraping.objects.SearchEngineQuery;
import ee.ttu.idk0071.sentiment.lib.scraping.objects.SearchEngineResult;
import ee.ttu.idk0071.sentiment.model.Business;
import ee.ttu.idk0071.sentiment.model.SentimentLookup;
import ee.ttu.idk0071.sentiment.model.SentimentLookupDomain;
import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;
import ee.ttu.idk0071.sentiment.model.SentimentType;
import ee.ttu.idk0071.sentiment.repository.BusinessRepository;
import ee.ttu.idk0071.sentiment.repository.SentimentLookupDomainRepository;
import ee.ttu.idk0071.sentiment.repository.SentimentLookupRepository;
import ee.ttu.idk0071.sentiment.repository.SentimentSnapshotRepository;
import ee.ttu.idk0071.sentiment.repository.SentimentTypeRepository;

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

	public SentimentLookup beginLookup(String businessName) {
		Business business = businessRepository.findByName(businessName);
		
		if (business == null) {
			business = new Business();
			business.setName(businessName);
			businessRepository.save(business);
		}
		
		SentimentLookup sentimentLookup = new SentimentLookup();
		sentimentLookup.setBusiness(business);
		sentimentLookup.setDate(new Date());
		sentimentLookupRepository.save(sentimentLookup);
		
		SearchEngineScraper scraper = new GoogleScraper();
		String queryString = businessName;
		SearchEngineQuery query = new SearchEngineQuery(queryString, 10);
		List<SearchEngineResult> searchLinks = scraper.search(query);
		
		float neuCnt = 0, posCnt = 0, negCnt = 0;
		Long rankNr = 1L;
		SentimentAnalyzer analyzer = new BasicSentimentAnalyzer(500);
		SentimentLookupDomain lookupDomain = sentimentLookupDomainRepository.findByName("Google");
		
		for (SearchEngineResult searchLink : searchLinks) {
			try {
				PageSentiment sentiment = analyzer.analyzePage(searchLink.getUrl());
				
				SentimentSnapshot snapshot = new SentimentSnapshot();
				snapshot.setRank(rankNr++);
				snapshot.setTitle(searchLink.getTitle());
				snapshot.setUrl(searchLink.getUrl());
				snapshot.setTrustLevel(sentiment.getTrustLevel());
				snapshot.setSentimentLookup(sentimentLookup);
				snapshot.setSentimentLookupDomain(lookupDomain);
				
				SentimentType pageSentimentType = null;
				switch (sentiment.getSentimentType()) {
					case NEUTRAL:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEUTRAL);
						neuCnt += sentiment.getTrustLevel() / 100;
						break;
					case POSITIVE:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_POSITIVE);
						posCnt += sentiment.getTrustLevel() / 100;
						break;
					case NEGATIVE:
						pageSentimentType = sentimentTypeRepository.findOne(SentimentType.TYPE_CODE_NEGATIVE);
						negCnt += sentiment.getTrustLevel() / 100;
						break;
					default:
						break;
				}
				
				snapshot.setSentimentType(pageSentimentType);
				snapshot.setSentimentLookupDomain(lookupDomain);
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
