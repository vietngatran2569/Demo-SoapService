package com.example.soapdemoaviet.service;

import com.example.soapdemoaviet.entity.Task;
import com.example.soapdemoaviet.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<Task> getAll() {
        return taskRepository.findAllByOrderByPriorityDesc();
    }
}

