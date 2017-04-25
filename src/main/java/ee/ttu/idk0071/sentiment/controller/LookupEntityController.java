package ee.ttu.idk0071.sentiment.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.controller.objects.MissingResourceException;
import ee.ttu.idk0071.sentiment.model.LookupEntity;
import ee.ttu.idk0071.sentiment.service.LookupEntityService;
import ee.ttu.idk0071.sentiment.service.objects.DomainLookupResult;

@RestController
public class LookupEntityController {
	@Autowired
	private LookupEntityService lookupEntityService;

	@RequestMapping(value="/lookup-entities/{id}/results", method=RequestMethod.GET)
	List<DomainLookupResult> getLookupStatistics(
			@PathVariable Long id, 
			@RequestParam(name="domainId", required=true) Integer domainId) throws MissingResourceException {
		LookupEntity lookupEntity = lookupEntityService.findById(id);
		if (lookupEntity == null)
			throw new MissingResourceException();
		return lookupEntityService.getResultsForDomain(lookupEntity, domainId);
	}

	@RequestMapping(value="/lookup-entities/results", method=RequestMethod.GET)
	List<DomainLookupResult> getLookupStatistics(
			@RequestParam(name="entityName", required=true) String rawEntityName, 
			@RequestParam(name="domainId", required=true) Integer domainId) throws MissingResourceException {
		LookupEntity lookupEntity = lookupEntityService.findByName(rawEntityName);
		if (lookupEntity == null)
			throw new MissingResourceException();
		return lookupEntityService.getResultsForDomain(lookupEntity, domainId);
	}

	@ExceptionHandler({MissingResourceException.class})
	void handleBadRequest(Throwable t, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value(), t.getMessage());
	}
}
