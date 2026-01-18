package com.thiagoaio.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var currentDate = LocalDateTime.now();
        if (
            currentDate.isAfter(taskModel.getStartAt()) || 
            currentDate.isAfter(taskModel.getEndAt()) || 
            taskModel.getStartAt().isAfter(taskModel.getEndAt())
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datas inválidas.");
        }

        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
}
