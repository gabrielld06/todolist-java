package com.supimpa.todolist.task;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.supimpa.todolist.exceptions.InvalidAttributeValue;
import com.supimpa.todolist.exceptions.InvalidEntityException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> create(@RequestBody TaskModel task, HttpServletRequest request) throws InvalidEntityException, InvalidAttributeValue {
        task.setUserId((UUID) request.getAttribute("userId"));

        // Data validation
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = task.getStartAt();
        LocalDateTime endDate = task.getEndAt();

        if(startDate.isBefore(currentDate)) {
            throw new InvalidAttributeValue("Start date cannot be in the past");
        }

        if(endDate.isBefore(startDate)) {
            throw new InvalidAttributeValue("End date shoud be after Start date");
        }

        try {
            this.taskRepository.save(task);
        } catch(Exception e) {
            throw new InvalidEntityException("Invalid task entity");
        }

        return Map.of(
            "message", "Task created successfully",
            "id", task.getId().toString(),
            "createdAt", task.getCreatedAt().toString()
        );
    }
}
