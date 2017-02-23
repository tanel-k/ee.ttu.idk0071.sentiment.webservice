package ee.ttu.idk0071.sentiment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.dao.BusinessTypeRepository;
import ee.ttu.idk0071.sentiment.model.BusinessType;

@Service
public class BusinessTypeService {
	@Autowired
	private BusinessTypeRepository businessTypeRepository;
	
	public List<BusinessType> getBusinessTypes() {
		return businessTypeRepository.findAll();
	}
	
	public BusinessType getBusinessType(int code) {
		return businessTypeRepository.findOne(code);
	}
}
