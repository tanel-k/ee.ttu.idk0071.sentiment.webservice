package ee.ttu.idk0071.sentiment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.model.Domain;
import ee.ttu.idk0071.sentiment.repository.DomainRepository;

@RestController
public class DomainController {
	@Autowired
	private DomainRepository domainRepository;

	@RequestMapping("/domains")
	List<Domain> getDomains() {
		return domainRepository.findByActiveTrue();
	}
}