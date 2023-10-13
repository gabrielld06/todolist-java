package com.supimpa.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import com.supimpa.todolist.exceptions.InvalidAttributeValue;
import com.supimpa.todolist.exceptions.InvalidEntityException;
import com.supimpa.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    private static final String TASK_NOT_FOUND = "Task not found";

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TaskModel create(@RequestBody TaskModel task, HttpServletRequest request) throws InvalidEntityException, InvalidAttributeValue {
        task.setUserId((UUID) request.getAttribute("userId"));

        // Data validation
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = task.getStartAt();
        LocalDateTime endDate = task.getEndAt();

        if(startDate.isBefore(currentDate)) {
            throw new InvalidAttributeValue("Start date cannot be in the past");
        }

        if(endDate.isBefore(startDate)) {
            throw new InvalidAttributeValue("End date should be after Start date");
        }

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
        UUID userId = (UUID) request.getAttribute("userId");

        return this.taskRepository.findByUserId(userId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TaskModel get(@PathVariable UUID id, HttpServletRequest request) throws EntityNotFoundException {
        return this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TaskModel update(@RequestBody TaskModel task, @PathVariable UUID id, HttpServletRequest request) throws EntityNotFoundException {
        Optional<TaskModel> taskToUpdate = this.taskRepository.findById(id);

        if(taskToUpdate.isEmpty()) {
            throw new EntityNotFoundException(TASK_NOT_FOUND);
        }

        Utils.copyNonNullPropertires(task, taskToUpdate);

        return this.taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, HttpServletRequest request) throws EntityNotFoundException {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TASK_NOT_FOUND));

        this.taskRepository.delete(task);
    }

}
