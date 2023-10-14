package com.supimpa.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.supimpa.todolist.exceptions.EntityNotFoundException;
import com.supimpa.todolist.exceptions.InvalidEntityException;
import com.supimpa.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    private static final String TASK_NOT_FOUND = "Task not found";
    private static final String USER_ID = "userId";

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TaskModel create(@RequestBody TaskModel task, HttpServletRequest request) throws InvalidEntityException {
        task.setUserId((UUID) request.getAttribute(USER_ID));

        try {
            return this.taskRepository.save(task);
        } catch(Exception e) {
            throw new InvalidEntityException("Invalid task entity");
        }
    }

    @GetMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<TaskModel> list(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute(USER_ID);

        return this.taskRepository.findByUserId(userId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TaskModel get(@PathVariable UUID id, HttpServletRequest request) throws EntityNotFoundException {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND));

        if(!task.getUserId().equals(request.getAttribute(USER_ID))) {
            throw new EntityNotFoundException(TASK_NOT_FOUND);
        }

        return task;
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TaskModel update(@RequestBody TaskModel task, @PathVariable UUID id, HttpServletRequest request) throws Throwable {
        TaskModel taskToUpdate = this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND));

        if(!taskToUpdate.getUserId().equals(request.getAttribute(USER_ID))) {
            throw new EntityNotFoundException(TASK_NOT_FOUND);
        }

        Utils.copyNonNullPropertires(task, taskToUpdate);

        try {
            return this.taskRepository.save(taskToUpdate);
        } catch(Exception e) {
            throw new InvalidEntityException("Invalid task entity");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, HttpServletRequest request) throws EntityNotFoundException {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND));

        if(!task.getUserId().equals(request.getAttribute(USER_ID))) {
            throw new EntityNotFoundException(TASK_NOT_FOUND);
        }

        this.taskRepository.delete(task);
    }

}
