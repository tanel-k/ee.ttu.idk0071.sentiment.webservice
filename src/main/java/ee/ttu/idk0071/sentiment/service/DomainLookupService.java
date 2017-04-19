package ee.ttu.idk0071.sentiment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.model.DomainLookup;
import ee.ttu.idk0071.sentiment.repository.DomainLookupRepository;
import ee.ttu.idk0071.sentiment.service.objects.MissingDomainLookupException;

@Service
public class DomainLookupService {
	@Autowired
	private DomainLookupRepository domainLookupRepository;

	public String getCurrentState(Long domainLookupId) throws MissingDomainLookupException {
		DomainLookup domainLookup = getById(domainLookupId);
		if (domainLookup == null)
			throw new MissingDomainLookupException(domainLookupId);
		
		return domainLookup.getDomainLookupState().getName();
	}

	public DomainLookup getById(Long domainLookupId){
		return domainLookupRepository.findOne(domainLookupId);
	}
}
