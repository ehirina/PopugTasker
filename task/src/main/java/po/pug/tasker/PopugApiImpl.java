package po.pug.tasker;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import po.pug.tasker.api.V1Api;
import po.pug.tasker.db.repository.AccountRepository;
import po.pug.tasker.db.repository.TaskRepository;
import po.pug.tasker.dto.CreateTaskBody;
import po.pug.tasker.dto.Popug;
import po.pug.tasker.dto.Task;
import po.pug.tasker.dto.TaskResponse;
import po.pug.tasker.queue.DebugAccountSender;

import java.util.List;
import java.util.stream.Collectors;

import static po.pug.tasker.TokenUtils.*;

@RestController
@RequiredArgsConstructor
public class PopugApiImpl implements V1Api {

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DebugAccountSender debugSender;

    @Override
    public ResponseEntity<Void> debug() {
        debugSender.send();
        return ResponseEntity.ok(null);
    }

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseEntity<TaskResponse> getTask(Integer taskId) {
        var token = getBearerTokenHeader();
        // todo: validate token, return unauthorized

        var task = taskRepository.getById(Long.valueOf(taskId));
        if (!getRole(token).equals("manager") && !task.getAssignee().getId().equals(getAccountId(token))) {
            // deny access to other's tasks for non-manager
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(taskEntityToTaskResponseDTO(task));
    }

    @Override
    public ResponseEntity<Void> completeTask(Integer taskId) {
        var token = getBearerTokenHeader();
        // todo: validate token, return unauthorized

        var task = taskRepository.getById(Long.valueOf(taskId));
        task.setStatus(po.pug.tasker.db.entity.Task.Status.DONE);
        taskRepository.save(task);

        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<TaskResponse> createTask(CreateTaskBody createTaskBody) {
        var task = new po.pug.tasker.db.entity.Task();
        task.setName(createTaskBody.getName());
        task.setDescription(createTaskBody.getDescription());
        task.setStatus(po.pug.tasker.db.entity.Task.Status.ASSIGNED);

        var accountCount = accountRepository.count();
        task.setAssignee(accountRepository.getAllBy().get(getRandomNumber(0, (int) accountCount)));

        taskRepository.save(task);

        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<List<Task>> getTasks() {
        var token = getBearerTokenHeader();
        // todo: validate token, return unauthorized

        var tasks = getRole(token).equals("manager") ? taskRepository.getAllBy() : taskRepository.getAllByAssigneeId(getAccountId(token));
        List<Task> result = tasks.stream().map(this::taskEntityToTaskDTO).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private Task taskEntityToTaskDTO(po.pug.tasker.db.entity.Task task) {
        var converted = new Task();
        converted.setId(task.getId());
        converted.setName(task.getName());
        converted.setDescription(task.getDescription());
        converted.setStatus(task.getStatus() == po.pug.tasker.db.entity.Task.Status.DONE ? Task.StatusEnum.DONE : Task.StatusEnum.ASSIGNED);
        return converted;
    }

    private TaskResponse taskEntityToTaskResponseDTO(po.pug.tasker.db.entity.Task task) {
        var converted = new TaskResponse();
        converted.setId(task.getId());
        converted.setName(task.getName());
        converted.setDescription(task.getDescription());
        converted.setStatus(task.getStatus() == po.pug.tasker.db.entity.Task.Status.DONE ? TaskResponse.StatusEnum.DONE : TaskResponse.StatusEnum.ASSIGNED);

        var popug = new Popug();
        popug.setId(task.getAssignee().getId());
        popug.setName(task.getAssignee().getName());
        popug.setRole(task.getAssignee().getRole().name());
        converted.setAssignee(popug);
        return converted;
    }
}
