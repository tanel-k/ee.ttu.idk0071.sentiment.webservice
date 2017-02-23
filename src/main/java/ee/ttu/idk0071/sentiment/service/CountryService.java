package ee.ttu.idk0071.sentiment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.dao.CountryRepository;
import ee.ttu.idk0071.sentiment.model.Country;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryRepository;
	
	public List<Country> getCountries() {
		return countryRepository.findAll();
	}
	
	public Country getCountry(String code) {
		return countryRepository.findOne(code);
	}
}
