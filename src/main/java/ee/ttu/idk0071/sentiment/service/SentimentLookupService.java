package ee.ttu.idk0071.sentiment.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.amqp.SentimentLookupDispatcher;
import ee.ttu.idk0071.sentiment.amqp.messages.SentimentLookupRequestMessage;
import ee.ttu.idk0071.sentiment.model.LookupEntity;
import ee.ttu.idk0071.sentiment.model.SentimentLookup;
import ee.ttu.idk0071.sentiment.repository.LookupEntityRepository;
import ee.ttu.idk0071.sentiment.repository.SentimentLookupRepository;

@Service
public class SentimentLookupService {
	@Autowired
	private SentimentLookupRepository sentimentLookupRepository;
	@Autowired
	private LookupEntityRepository lookupEntityRepository;
	@Autowired
	private SentimentLookupDispatcher lookupDispatcher;

	public SentimentLookup getLookup(Long id) {
		return sentimentLookupRepository.findOne(id);
	}

	public SentimentLookup beginLookup(String entityName) {
		LookupEntity entity = lookupEntityRepository.findByName(entityName);
		
		if (entity == null) {
			entity = new LookupEntity();
			entity.setName(entityName);
			lookupEntityRepository.save(entity);
		}
		
		SentimentLookup sentimentLookup = new SentimentLookup();
		sentimentLookup.setEntity(entity);
		sentimentLookup.setDate(new Date());
		sentimentLookupRepository.save(sentimentLookup);
		
		SentimentLookupRequestMessage lookupMessage = new SentimentLookupRequestMessage();
		lookupMessage.setLookupId(sentimentLookup.getId());
		lookupDispatcher.requestLookup(lookupMessage);
		
		return sentimentLookup;
	}
}
