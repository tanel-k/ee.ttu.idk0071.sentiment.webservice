package ee.ttu.idk0071.sentiment.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.ttu.idk0071.sentiment.model.Business;
import ee.ttu.idk0071.sentiment.model.BusinessType;
import ee.ttu.idk0071.sentiment.model.Country;

public interface BusinessRepository extends CrudRepository<Business, Long> {
	@Override
	List<Business> findAll();

	Business findByCountryAndBusinessTypeAndBusinessName(Country country, BusinessType businessType, String businessName);
}
