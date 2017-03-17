package ee.ttu.idk0071.sentiment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.model.SentimentType;
import ee.ttu.idk0071.sentiment.repository.SentimentTypeRepository;

@Service
public class SentimentTypeService {
	@Autowired
	private SentimentTypeRepository sentimentTypeRepository;

	public List<SentimentType> getSentimentTypes() {
		return sentimentTypeRepository.findAll();
	}

	public SentimentType getSentimentType(String code) {
		return sentimentTypeRepository.findOne(code);
	}
}
