package ee.ttu.idk0071.sentiment.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentimentLookupDispatcherConfiguration extends MessageConfiguration {
	protected final String lookupQueue = "lookup-request-queue";

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setRoutingKey(lookupQueue);
		template.setQueue(this.lookupQueue);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	@Bean
	public Queue lookupQueue() {
		return new Queue(this.lookupQueue, true);
	}
}
