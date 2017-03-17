package ee.ttu.idk0071.sentiment.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.amqp.LookupDispatcher;
import ee.ttu.idk0071.sentiment.amqp.messages.LookupRequestMessage;
import ee.ttu.idk0071.sentiment.model.LookupEntity;
import ee.ttu.idk0071.sentiment.model.Lookup;
import ee.ttu.idk0071.sentiment.repository.LookupEntityRepository;
import ee.ttu.idk0071.sentiment.repository.LookupRepository;
import ee.ttu.idk0071.sentiment.repository.LookupStateRepository;

@Service
public class SentimentLookupService {
	@Autowired
	private LookupRepository lookupRepository;
	@Autowired
	private LookupEntityRepository lookupEntityRepository;
	@Autowired
	private LookupStateRepository lookupStateRepository;
	@Autowired
	private LookupDispatcher lookupDispatcher;

	public Lookup getLookup(Long id) {
		return lookupRepository.findOne(id);
	}

	public Lookup beginLookup(String entityName) {
		LookupEntity entity = lookupEntityRepository.findByName(entityName);
		
		if (entity == null) {
			entity = new LookupEntity();
			entity.setName(entityName);
			lookupEntityRepository.save(entity);
		}
		
		Lookup lookup = new Lookup();
		lookup.setLookupEntity(entity);
		lookup.setDate(new Date());
		lookup.setLookupState(lookupStateRepository.findByName("Queued"));
		lookupRepository.save(lookup);
		
		LookupRequestMessage lookupMessage = new LookupRequestMessage();
		lookupMessage.setLookupId(lookup.getId());
		lookupDispatcher.requestLookup(lookupMessage);
		
		return lookup;
	}
}
