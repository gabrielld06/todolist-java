package com.supimpa.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.supimpa.todolist.exceptions.InvalidAttributeValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="tb_task")
public class TaskModel {

    @Id @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public void setTitle(String title) throws InvalidAttributeValue {
        if(title.length() > 50) {
            throw new InvalidAttributeValue("Title must have less than 50 characters");
        }

        this.title = title;
    }

    public void setStartAt(LocalDateTime startAt) throws InvalidAttributeValue {
        if(startAt == null) {
            throw new InvalidAttributeValue("StartAt can't be null");
        }

        if(this.getEndAt() != null && this.getEndAt().isBefore(startAt)) {
            throw new InvalidAttributeValue("StartAt should be before EndAt");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if(startAt.isBefore(currentDate)) {
            throw new InvalidAttributeValue("StartAt cannot be in the past");
        }

        this.startAt = startAt;
    }

    public void setEndAt(LocalDateTime endAt) throws InvalidAttributeValue {
        if(endAt == null) {
            throw new InvalidAttributeValue("EndAt can't be null");
        }

        if(this.getStartAt() != null && this.getStartAt().isAfter(endAt)) {
            throw new InvalidAttributeValue("StartAt should be before EndAt");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if(endAt.isBefore(currentDate)) {
            throw new InvalidAttributeValue("EndAt cannot be in the past");
        }

        this.endAt = endAt;
    }

}
