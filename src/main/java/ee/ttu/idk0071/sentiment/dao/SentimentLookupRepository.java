package ee.ttu.idk0071.sentiment.dao;

import org.springframework.data.repository.CrudRepository;

import ee.ttu.idk0071.sentiment.model.SentimentLookup;

public interface SentimentLookupRepository extends CrudRepository<SentimentLookup, Long>{

}
