package ee.ttu.idk0071.sentiment.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.controller.messages.LookupRequest;
import ee.ttu.idk0071.sentiment.controller.messages.LookupResponse;
import ee.ttu.idk0071.sentiment.model.Lookup;
import ee.ttu.idk0071.sentiment.service.SentimentLookupService;

@RestController
public class SentimentLookupController {
	@Autowired
	private SentimentLookupService sentimentLookupService;

	@RequestMapping("/lookups/{id}")
	public Lookup getLookup(@PathVariable Long id) {
		return sentimentLookupService.getLookup(id);
	}

	@RequestMapping(value="/lookups", method=RequestMethod.POST)
	public LookupResponse createLookup(@RequestBody LookupRequest lookupRequest) {
		Lookup newLookup = sentimentLookupService.beginLookup(
			lookupRequest.getEntityName(),
			lookupRequest.getDomainIds());
		
		LookupResponse response = new LookupResponse();
		response.setLookupId(newLookup.getId());
		response.setDomainLookupIds(newLookup.getDomainLookups().stream()
			.map(domainLookup -> domainLookup.getId()).collect(Collectors.toList()));
		return response;
	}
}
