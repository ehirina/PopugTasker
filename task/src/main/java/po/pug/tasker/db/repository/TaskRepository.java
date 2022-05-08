package po.pug.tasker.db.repository;

import org.springframework.data.repository.CrudRepository;
import po.pug.tasker.db.entity.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> getAllBy();
    List<Task> getAllByAssigneeId(Long assigneeId);
    Task getById(Long taskId);
}
