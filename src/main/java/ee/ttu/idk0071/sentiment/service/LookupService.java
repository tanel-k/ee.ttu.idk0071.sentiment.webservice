package ee.ttu.idk0071.sentiment.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.lib.messages.DomainLookupRequestMessage;
import ee.ttu.idk0071.sentiment.messaging.dispatcher.LookupDispatcher;
import ee.ttu.idk0071.sentiment.model.Domain;
import ee.ttu.idk0071.sentiment.model.DomainLookup;
import ee.ttu.idk0071.sentiment.model.Lookup;
import ee.ttu.idk0071.sentiment.model.LookupEntity;
import ee.ttu.idk0071.sentiment.repository.DomainLookupRepository;
import ee.ttu.idk0071.sentiment.repository.DomainLookupStateRepository;
import ee.ttu.idk0071.sentiment.repository.DomainRepository;
import ee.ttu.idk0071.sentiment.repository.LookupEntityRepository;
import ee.ttu.idk0071.sentiment.repository.LookupRepository;

@Service
public class LookupService {
	@Autowired
	private LookupRepository lookupRepository;
	@Autowired
	private LookupEntityRepository lookupEntityRepository;
	@Autowired
	private DomainLookupStateRepository domainLookupStateRepository;

	@Autowired
	private DomainLookupRepository domainLookupRepository;
	@Autowired
	private DomainRepository domainRepository;

	@Autowired
	private LookupDispatcher lookupDispatcher;

	public Lookup getLookup(Long id) {
		return lookupRepository.findOne(id);
	}

	public Lookup beginLookup(String entityName, List<Integer> domainIds) {
		String normalizedEntityName = normalizeEntityName(entityName);
		LookupEntity entity = lookupEntityRepository.findByName(normalizedEntityName);
		
		// find target of search
		if (entity == null) {
			// create target if not exists
			entity = new LookupEntity();
			entity.setName(normalizedEntityName);
			lookupEntityRepository.save(entity);
		}
		
		// one parent lookup
		Lookup lookup = new Lookup();
		lookup.setLookupEntity(entity);
		lookup.setDate(new Date());
		lookupRepository.save(lookup);
		
		for (Integer domainId : domainIds) {
			// one sub-lookup per domain
			Domain domain = domainRepository.findOne(domainId);
			DomainLookup domainLookup = new DomainLookup();
			domainLookup.setDomain(domain);
			domainLookup.setLookup(lookup);
			domainLookup.setDomainLookupState(domainLookupStateRepository.findByName("Queued"));
			domainLookupRepository.save(domainLookup);
			
			// both sides of a relationship need to be updated
			lookup.getDomainLookups().add(domainLookup);
			
			// dispatch message
			DomainLookupRequestMessage lookupMessage = new DomainLookupRequestMessage();
			lookupMessage.setDomainLookupId(domainLookup.getId());
			lookupDispatcher.requestLookup(lookupMessage);
		}
		
		return lookupRepository.findOne(lookup.getId());
	}

	private static String normalizeEntityName(String entityName) {
		return entityName.toLowerCase().trim().replaceAll(" +", " ");
	}
}
