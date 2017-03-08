package ee.ttu.idk0071.sentiment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.model.BusinessType;
import ee.ttu.idk0071.sentiment.service.BusinessTypeService;

@RestController
public class BusinessTypeController {
	@Autowired
	private BusinessTypeService businessTypeService;

	@RequestMapping("/classifiers/business-types")
	public List<BusinessType> getBusinessTypes() {
		return businessTypeService.getBusinessTypes();
	}

	@RequestMapping("/classifiers/business-types/{id}")
	public BusinessType getBusinessTypes(@PathVariable int id) {
		return businessTypeService.getBusinessType(id);
	}
}
