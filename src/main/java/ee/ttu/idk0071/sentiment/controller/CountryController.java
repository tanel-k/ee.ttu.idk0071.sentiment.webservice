package ee.ttu.idk0071.sentiment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.model.Country;
import ee.ttu.idk0071.sentiment.service.CountryService;

@RestController
public class CountryController {
	private CountryService countryService;
	
	@RequestMapping("/classifiers/countries")
	public List<Country> getCountries() {
		return countryService.getCountries();
	}
	
	@RequestMapping("/classifiers/countries/{code}")
	public Country getCountry(@PathVariable String code) {
		return countryService.getCountry(code);
	}
}
