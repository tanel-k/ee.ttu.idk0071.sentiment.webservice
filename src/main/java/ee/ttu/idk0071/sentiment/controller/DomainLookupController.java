package ee.ttu.idk0071.sentiment.controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ee.ttu.idk0071.sentiment.service.DomainLookupService;
import ee.ttu.idk0071.sentiment.service.objects.MissingDomainLookupException;

@RestController
@EnableScheduling
public class DomainLookupController {
	@Autowired
	private DomainLookupService domainLookupService;

	@GetMapping("/domain-lookups/{domainLookupId}/updates")
	public SseEmitter status(@PathVariable Long domainLookupId) {
		final SseEmitter updateEmitter = new SseEmitter(60*1000L);
		final ScheduledExecutorService  scheduler = Executors.newSingleThreadScheduledExecutor();
		
		updateEmitter.onCompletion(() -> scheduler.shutdown());
		updateEmitter.onTimeout(() -> scheduler.shutdown());
		scheduler.scheduleAtFixedRate(() -> {
		
			try {
				
				String state = domainLookupService.getCurrentState(domainLookupId);
				updateEmitter.send(state, MediaType.TEXT_PLAIN);
				
			} catch (MissingDomainLookupException ex) {
				updateEmitter.completeWithError(ex);
			} catch (IOException ex) {
				scheduler.shutdown();
			}
		
		}, 0, 5, TimeUnit.SECONDS);
		
		return updateEmitter;
	}
}
