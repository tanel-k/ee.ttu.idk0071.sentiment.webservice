package ee.ttu.idk0071.sentiment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.lib.messages.DomainLookupRequestMessage;
import ee.ttu.idk0071.sentiment.messaging.dispatcher.LookupDispatcher;
import ee.ttu.idk0071.sentiment.model.DomainLookup;
import ee.ttu.idk0071.sentiment.model.DomainLookupState;
import ee.ttu.idk0071.sentiment.repository.DomainLookupRepository;
import ee.ttu.idk0071.sentiment.repository.DomainLookupStateRepository;
import ee.ttu.idk0071.sentiment.service.objects.InvalidStateTransitionException;
import ee.ttu.idk0071.sentiment.service.objects.MissingDomainLookupException;

@Service
public class DomainLookupService {
	public static final Integer STATE_CODE_ERROR = 4;
	public static final Integer STATE_CODE_QUEUED = 1;

	@Autowired
	private DomainLookupRepository domainLookupRepository;
	@Autowired
	private DomainLookupStateRepository domainLookupStateRepository;

	@Autowired
	private LookupDispatcher lookupDispatcher;

	public String getCurrentState(Long domainLookupId) throws MissingDomainLookupException {
		DomainLookup domainLookup = getById(domainLookupId);
		if (domainLookup == null)
			throw new MissingDomainLookupException(domainLookupId);
		
		return domainLookup.getDomainLookupState().getName();
	}

	public DomainLookup getById(Long domainLookupId){
		return domainLookupRepository.findOne(domainLookupId);
	}

	public void restartDomainLookup(Long domainLookupId)
			throws MissingDomainLookupException, InvalidStateTransitionException {
		DomainLookup domainLookup = getById(domainLookupId);
		if (domainLookup == null)
			throw new MissingDomainLookupException(domainLookupId);
		
		DomainLookupState currentState = domainLookup.getDomainLookupState();
		if (currentState.getCode() != STATE_CODE_ERROR)
			throw new InvalidStateTransitionException(currentState.getCode(), STATE_CODE_QUEUED);
		
		DomainLookupState queuedState = domainLookupStateRepository.findOne(STATE_CODE_QUEUED);
		domainLookup.setDomainLookupState(queuedState);
		domainLookupRepository.save(domainLookup);
		
		DomainLookupRequestMessage lookupMessage = new DomainLookupRequestMessage();
		lookupMessage.setDomainLookupId(domainLookup.getId());
		lookupDispatcher.requestLookup(lookupMessage);
	}
}
