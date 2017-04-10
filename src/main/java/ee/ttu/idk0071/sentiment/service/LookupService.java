package ee.ttu.idk0071.sentiment.service;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
import ee.ttu.idk0071.sentiment.service.objects.InvalidRequestException;

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

	/**
	 * Triggers a lookup for the specified entity name.<br/>
	 * For each domainId, a domain lookup is initiated and a message is dispatched to all executors.
	 */
	public Lookup beginLookup(String entityName, List<Integer> domainIds) throws InvalidRequestException {
		if (StringUtils.isEmpty(entityName)) {
			throw new InvalidRequestException("Entity name may not be empty");
		}
		
		if (domainIds == null || domainIds.isEmpty()) {
			throw new InvalidRequestException("At least one search domain needs to be provided");
		}
		
		List<Domain> searchDomains = mapDomainIdsToDomains(domainIds);
		
		if (containsInactiveDomains(searchDomains)) {
			throw new InvalidRequestException("An inactive domain was included in the request");
		}
		
		if (containsNulls(searchDomains)) {
			throw new InvalidRequestException("An unknown domain id was included in the request");
		}
		
		String normalizedEntityName = normalizeEntityName(entityName);
		LookupEntity lookupEntity = getOrCreateLookupEntity(normalizedEntityName);
		
		// one parent lookup
		Lookup lookup = new Lookup();
		lookup.setLookupEntity(lookupEntity);
		lookup.setDate(new Date());
		lookupRepository.save(lookup);
		
		for (Domain domain : searchDomains) {
			// one sub-lookup per domain
			DomainLookup domainLookup = new DomainLookup();
			domainLookup.setDomain(domain);
			domainLookup.setLookup(lookup);
			domainLookup.setDomainLookupState(domainLookupStateRepository.findByName("Queued"));
			domainLookupRepository.save(domainLookup);
			
			// both sides of a relationship need to be updated
			lookup.getDomainLookups().add(domainLookup);
			
			// dispatch message to executors
			DomainLookupRequestMessage lookupMessage = new DomainLookupRequestMessage();
			lookupMessage.setDomainLookupId(domainLookup.getId());
			lookupDispatcher.requestLookup(lookupMessage);
		}
		
		return lookupRepository.findOne(lookup.getId());
	}

	private LookupEntity getOrCreateLookupEntity(String normalizedEntityName) {
		LookupEntity entity = lookupEntityRepository.findByName(normalizedEntityName);
		// find target of search
		if (entity == null) {
			// create target if not exists
			entity = new LookupEntity();
			entity.setName(normalizedEntityName);
			lookupEntityRepository.save(entity);
		}
		
		return entity;
	}

	private List<Domain> mapDomainIdsToDomains(List<Integer> domainIds) throws InvalidRequestException {
		List<Domain> searchDomains = domainIds.stream()
				.map(id -> domainRepository.findOne(id))
				.collect(Collectors.toList());
		return searchDomains;
	}

	/**
	 * Filters the input Domain list and returns true if any domain is inactive.
	 */
	public static boolean containsInactiveDomains(List<Domain> domains) {
		Predicate<Domain> inactiveDomainFilter = (d -> d != null && !d.isActive());
		return domains.stream().anyMatch(inactiveDomainFilter);
	}

	/**
	 * Filters the input Object list and returns true if any item is null.
	 */
	public static <T extends Object> boolean containsNulls(List<T> objects) {
		Predicate<T> nullFilter = (o -> o == null);
		return objects.stream().anyMatch(nullFilter);
	}

	/**
	 * Given a raw entity name, this method will:<br/>
	 * <ol>
	 *  <li>compress extraneous whitespace characters</li>
	 *  <li>remove leading/trailing whitespace characters</li>
	 *  <li>switch the string to lower case</li>
	 * </ol>
	 * The resultant entity name is ready for insertion into the database.
	 */
	public static String normalizeEntityName(String entityName) {
		return entityName.toLowerCase().trim().replaceAll("\\s+", " ");
	}
}
