package ee.ttu.idk0071.sentiment.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;

public interface SentimentSnapshotRepository extends CrudRepository<SentimentSnapshot, Long>{
	public List<SentimentSnapshot> findBySentimentLookupId(Long lookupId);
}
