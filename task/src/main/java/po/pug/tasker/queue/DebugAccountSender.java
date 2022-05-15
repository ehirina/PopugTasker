package po.pug.tasker.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class DebugAccountSender {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    public void send() {
        String message = "{\n" +
                "  \"eventName\": \"User.Created\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 66,\n" +
                "    \"name\": \"Popugello\",\n" +
                "    \"role\": \"popug\"\n" +
                "  }\n" +
                "}";
        this.template.convertAndSend(queue.getName(), message);

        message = "{\n" +
                "  \"eventName\": \"User.Created\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 67,\n" +
                "    \"name\": \"Popughissimo\",\n" +
                "    \"role\": \"manager\"\n" +
                "  }\n" +
                "}";
        this.template.convertAndSend(queue.getName(), message);
    }
}
