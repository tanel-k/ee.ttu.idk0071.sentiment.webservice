package ee.ttu.idk0071.sentiment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ttu.idk0071.sentiment.model.SentimentSnapshot;
import ee.ttu.idk0071.sentiment.service.SentimentSnapshotService;

@RestController
public class SentimentSnapshotController {
	@Autowired
	private SentimentSnapshotService sentimentSnapshotService;

	@RequestMapping("/lookups/{lookupId}/snapshots")
	public List<SentimentSnapshot> getSnapshots(@PathVariable Long lookupId) {
		return sentimentSnapshotService.getLookupSnapshots(lookupId);
	}
}
