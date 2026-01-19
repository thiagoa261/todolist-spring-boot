package com.thiagoaio.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiagoaio.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @GetMapping("")
    public ResponseEntity listTasks(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId((UUID) userId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

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

    @PutMapping("/update/{id}")
    public ResponseEntity updateTask(
        @RequestBody TaskModel taskModel, 
        HttpServletRequest request,
        @PathVariable UUID id
    ) {
        var task = this.taskRepository.findById(id).orElse(null);
        if (task == null) 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada.");
        
        var userId = request.getAttribute("userId");
        if(!task.getUserId().equals((UUID) userId)) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");

        Utils.copyNonNullProperties(taskModel, task);

        var newTask = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }
}