package ee.ttu.idk0071.sentiment.amqp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ee.ttu.idk0071.sentiment.amqp.messages.LookupRequestMessage;

@Component
public class LookupDispatcher {
	@Autowired
	private LookupDispatcherConfiguration lookupDispatcherConfiguration;
	@Autowired
	private ConnectionFactory connectionFactory;

	public void requestLookup(LookupRequestMessage lookupMessage) {
		lookupDispatcherConfiguration
			.rabbitTemplate(connectionFactory)
			.convertAndSend(
				lookupDispatcherConfiguration.lookupQueue, 
				lookupMessage);
	}
}
