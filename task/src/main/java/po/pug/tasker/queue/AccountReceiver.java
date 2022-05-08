package po.pug.tasker.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import po.pug.tasker.db.entity.Account;
import po.pug.tasker.db.repository.AccountRepository;

@Slf4j
@RabbitListener(queues = "accounts")
public class AccountReceiver {

    @Autowired
    private AccountRepository accountRepository;

//    @RabbitHandler
//    public void receive(String in) {
//        Long accountId = parseEvent(in);
//        log.debug("Received User.Created event for ID '" + accountId + "'");
//
//        if (accountId == null) return;
//        var account = new Account();
//        account.setId(accountId);
//        // TODO map other fields
//        accountRepository.save(account);
//        log.debug("Added account with ID " + accountId);
//    }

    @RabbitHandler
    public void receive(Object in) {
        Message message = (Message) in;
        log.debug("works?");
        String string = new String(message.getBody());
        log.debug(string);
        var parsedAccount = parseEvent(string);
        if (parsedAccount == null) return;

        log.debug("Received User.Created event for ID '" + parsedAccount.id + "'");

        var account = new Account();
        account.setId(parsedAccount.id);
        account.setName(parsedAccount.name);
        account.setRole(parsedAccount.role.equals("ADMIN") ? Account.Role.ADMIN : Account.Role.POPUG);
        accountRepository.save(account);
        log.debug("Added account with ID " + parsedAccount.id);
    }

    private UserCreatedEventData parseEvent(String event) {
        var mapper = new ObjectMapper();
        try {
            var parsedPayload = mapper.readValue(event, UserCreatedEvent.class);
            return parsedPayload.data;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class UserCreatedEvent {
        String eventName;
        UserCreatedEventData data;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class UserCreatedEventData {
        Long id;
        String name;
        String role;
    }
}
