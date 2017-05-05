package ee.ttu.idk0071.sentiment.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.controller.messages.LookupRequest;
import ee.ttu.idk0071.sentiment.controller.messages.LookupResponse;
import ee.ttu.idk0071.sentiment.model.Lookup;
import ee.ttu.idk0071.sentiment.service.LookupService;
import ee.ttu.idk0071.sentiment.service.objects.InvalidRequestException;

@RestController
public class LookupController {
	@Autowired
	private LookupService lookupService;

	@RequestMapping("/lookups/{id}")
	Lookup getLookup(@PathVariable Long id) {
		return lookupService.getLookup(id);
	}

	@RequestMapping(value="/lookups", method=RequestMethod.POST)
	LookupResponse createLookup(@RequestBody LookupRequest lookupRequest) throws InvalidRequestException {
		Lookup newLookup = lookupService.beginLookup(
				lookupRequest.getEntityName(),
				lookupRequest.getEmail(),
				lookupRequest.getDomainIds());
		
		LookupResponse response = new LookupResponse();
		response.setLookupId(newLookup.getId());
		response.setDomainLookupIds(newLookup.getDomainLookups().stream()
			.map(domainLookup -> domainLookup.getId()).collect(Collectors.toList()));
		return response;
	}

	@ExceptionHandler({IllegalArgumentException.class, InvalidRequestException.class})
	void handleBadRequest(Throwable t, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), t.getMessage());
	}
}
