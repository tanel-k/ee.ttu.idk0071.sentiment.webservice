package ee.ttu.idk0071.sentiment.controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${domain-lookups.update-rate-seconds}")
	private Long updateRate;
	@Value("${domain-lookups.sse-timeout-millis}")
	private Long sseTimeout;

	@Autowired
	private DomainLookupService domainLookupService;

	@GetMapping("/domain-lookups/{domainLookupId}/updates")
	public SseEmitter status(@PathVariable Long domainLookupId) {
		SseEmitter updateEmitter = new SseEmitter(sseTimeout);
		ScheduledExecutorService  scheduler = Executors.newSingleThreadScheduledExecutor();
		
		updateEmitter.onCompletion(() -> scheduler.shutdown());
		updateEmitter.onTimeout(() -> scheduler.shutdown());
		
		scheduler.scheduleAtFixedRate(() -> {
			try {
				String state;
				
				try {
					state = domainLookupService.getCurrentState(domainLookupId);
				} catch(MissingDomainLookupException ex) {
					updateEmitter.completeWithError(ex);
					return;
				}
				
				updateEmitter.send(SseEmitter.event().name("state").data(state));
			} catch (IOException ex) {
				scheduler.shutdown();
			}
		
		}, 0, updateRate, TimeUnit.SECONDS);
		
		return updateEmitter;
	}
}
