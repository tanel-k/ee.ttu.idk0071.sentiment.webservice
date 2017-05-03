package ee.ttu.idk0071.sentiment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.repository.ErrorLogRepository;

@Service
public class ErrorService {
	
	@Autowired
	private ErrorLogRepository errorLogRepository;
	
}
