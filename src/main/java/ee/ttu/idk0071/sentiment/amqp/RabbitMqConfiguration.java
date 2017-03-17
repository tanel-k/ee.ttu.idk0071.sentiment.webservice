package ee.ttu.idk0071.sentiment.amqp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {
	@Bean
	public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
		return null;
	}
}
