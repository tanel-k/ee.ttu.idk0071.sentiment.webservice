package ee.ttu.idk0071.sentiment.service;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import ee.ttu.idk0071.sentiment.lib.messages.DomainLookupRequestMessage;
import ee.ttu.idk0071.sentiment.messaging.dispatcher.LookupDispatcher;
import ee.ttu.idk0071.sentiment.model.Domain;
import ee.ttu.idk0071.sentiment.model.DomainLookup;
import ee.ttu.idk0071.sentiment.model.DomainLookupState;
import ee.ttu.idk0071.sentiment.model.Lookup;
import ee.ttu.idk0071.sentiment.model.LookupEntity;
import ee.ttu.idk0071.sentiment.repository.DomainLookupRepository;
import ee.ttu.idk0071.sentiment.repository.DomainLookupStateRepository;
import ee.ttu.idk0071.sentiment.repository.DomainRepository;
import ee.ttu.idk0071.sentiment.repository.LookupEntityRepository;
import ee.ttu.idk0071.sentiment.repository.LookupRepository;
import ee.ttu.idk0071.sentiment.service.objects.InvalidRequestException;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(LookupService.class)
public class LookupServiceTests {
	@InjectMocks
	private LookupService lookupService = new LookupService();

	@Mock
	private LookupRepository lookupRepository;
	@Mock
	private DomainRepository domainRepository;
	@Mock
	private DomainLookupRepository domainLookupRepository;
	@Mock
	private DomainLookupStateRepository domainLookupStateRepository;
	@Mock
	private LookupEntityRepository lookupEntityRepository;
	@Mock
	private LookupDispatcher lookupDispatcher;

	@Test(expected=InvalidRequestException.class)
	public void testEmptyEntityNameIsRejected() throws InvalidRequestException {
		String entityName = "";
		lookupService.beginLookup(entityName, null);
	}

	@Test(expected=InvalidRequestException.class)
	public void testEmptyDomainListIsRejected() throws InvalidRequestException {
		String entityName = "testName";
		List<Integer> domainIds = Lists.emptyList();
		lookupService.beginLookup(entityName, domainIds);
	}

	@Test(expected=InvalidRequestException.class)
	public void testInactiveDomainIsRejected() throws InvalidRequestException {
		int inactiveDomainId = 1;
		Domain inactiveDomain = new Domain();
		inactiveDomain.setActive(false);
		Mockito.when(domainRepository.findOne(inactiveDomainId)).thenReturn(inactiveDomain);
		
		String entityName = "testName";
		List<Integer> domainIds = Lists.newArrayList(inactiveDomainId);
		lookupService.beginLookup(entityName, domainIds);
	}

	@Test(expected=InvalidRequestException.class)
	public void testUnknownDomainIsRejected() throws InvalidRequestException {
		int unknownDomainId = 1;
		Mockito.when(domainRepository.findOne(unknownDomainId)).thenReturn(null);
		
		String entityName = "testName";
		List<Integer> domainIds = Lists.newArrayList(unknownDomainId);
		lookupService.beginLookup(entityName, domainIds);
	}

	@Test
	public void testEntityNameNormalization() {
		String unnormalizedName = "   \tUncle   Joe's  ";
		String normalizedName = "uncle joe's";
		Assert.assertEquals(normalizedName, LookupEntityService.normalizeEntityName(unnormalizedName));
	}

	@Test
	public void testLookupSubmission() throws Exception {
		String entityName = "testName";
		int domainId = 1;
		long domainLookupId = 1;
		
		Domain domain = new Domain();
		domain.setActive(true);
		
		LookupEntity lookupEntity = new LookupEntity();
		lookupEntity.setName(LookupEntityService.normalizeEntityName(entityName));
		
		DomainLookupState initialDomainLookupState = new DomainLookupState();
		DomainLookup domainLookup = new DomainLookup();
		domainLookup.setId(domainLookupId);
		
		Mockito.when(lookupEntityRepository.findByName(entityName)).thenReturn(lookupEntity);
		Mockito.when(domainLookupStateRepository.findByName(LookupService.DOMAIN_LOOKUP_INITIAL_STATE)).thenReturn(initialDomainLookupState);
		Mockito.when(domainRepository.findOne(domainId)).thenReturn(domain);
		
		PowerMockito.whenNew(DomainLookup.class).withNoArguments().thenReturn(domainLookup);
		ArgumentCaptor<DomainLookupRequestMessage> domainLookupRequestMsgCaptor = ArgumentCaptor.forClass(DomainLookupRequestMessage.class);
		
		Lookup lookupResult = lookupService.beginLookup(entityName, Lists.newArrayList(domainId));

		// check dispatch occurred
		Mockito.verify(lookupDispatcher, Mockito.times(1)).requestLookup(domainLookupRequestMsgCaptor.capture());
		List<DomainLookupRequestMessage> cappedDomainLookupRequestMessages = domainLookupRequestMsgCaptor.getAllValues();
		Assert.assertTrue(cappedDomainLookupRequestMessages.stream().anyMatch(msg -> domainLookupId == msg.getDomainLookupId()));
		
		// check # of domainlookups
		Assert.assertEquals(lookupResult.getDomainLookups().size(), 1);
		// check lookupEntity
		Assert.assertEquals(lookupResult.getLookupEntity().getName(), lookupEntity.getName());
		// check two-way link between lookup and domain-lookup
		Assert.assertTrue(lookupResult.getDomainLookups().stream().allMatch(dl -> dl.getLookup().equals(lookupResult)));
	}
}
