package po.pug.tasker.db.repository;

import org.springframework.data.repository.CrudRepository;
import po.pug.tasker.db.entity.Account;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> getAllBy();
}
