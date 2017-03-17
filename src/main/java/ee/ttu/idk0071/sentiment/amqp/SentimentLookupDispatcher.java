package ee.ttu.idk0071.sentiment.amqp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ee.ttu.idk0071.sentiment.amqp.messages.SentimentLookupRequestMessage;

@Component
public class SentimentLookupDispatcher {
	@Autowired
	private SentimentLookupDispatcherConfiguration lookupDispatcherConfiguration;
	@Autowired
	private ConnectionFactory connectionFactory;

	public void requestLookup(SentimentLookupRequestMessage lookupMessage) {
		lookupDispatcherConfiguration
			.rabbitTemplate(connectionFactory)
			.convertAndSend(
				lookupDispatcherConfiguration.lookupQueue, 
				lookupMessage);
	}
}
