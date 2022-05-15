package po.pug.tasker.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue accounts(){
        return new Queue("accounts");
    }

    @Bean AccountReceiver accountReceiver() {
        return new AccountReceiver();
    }

    @Bean
    DebugAccountSender accountSender() {
        return new DebugAccountSender();
    }
}
