package ee.ttu.idk0071.sentiment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.controller.objects.SentimentLookupRequest;
import ee.ttu.idk0071.sentiment.model.SentimentLookup;
import ee.ttu.idk0071.sentiment.service.BusinessTypeService;
import ee.ttu.idk0071.sentiment.service.CountryService;
import ee.ttu.idk0071.sentiment.service.SentimentLookupService;

@RestController
public class SentimentLookupController {
	@Autowired
	private CountryService countryService;
	@Autowired
	private BusinessTypeService businessTypeService;
	@Autowired
	private SentimentLookupService sentimentLookupService;

	@RequestMapping("/lookups/{id}")
	public SentimentLookup getLookup(@PathVariable Long id) {
		return sentimentLookupService.getLookup(id);
	}

	@RequestMapping(value="/lookups", method=RequestMethod.POST)
	public SentimentLookup createLookup(@RequestBody SentimentLookupRequest lookupRequest) {
		return sentimentLookupService.beginLookup(
			countryService.getCountry(lookupRequest.getCountryCode()), 
			businessTypeService.getBusinessType(lookupRequest.getBusinessTypeId()), 
			lookupRequest.getBusinessName());
	}
}
