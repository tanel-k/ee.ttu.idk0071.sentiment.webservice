package ee.ttu.idk0071.sentiment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.ttu.idk0071.sentiment.dao.SentimentSnapshotRepository;
import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;

@Service
public class SentimentSnapshotService {
	@Autowired
	private SentimentSnapshotRepository sentimentSnapshotRepository;

	public List<SentimentSnapshot> getLookupSnapshots(Long sentimentLookupId) {
		return sentimentSnapshotRepository.findBySentimentLookupId(sentimentLookupId);
	}
}
