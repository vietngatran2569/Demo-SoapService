package com.example.soapdemoaviet.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private Integer priority;
    private String priorityName;
    private Long personId;
    private String personName;
    private String bootstrap;

    public TaskDTO(Task task) {
        BeanUtils.copyProperties(task, this);
        Person person = task.getPerson();
        this.personId = person.getId();
        this.personName = person.getName();
        this.priorityName = Task.PriorityLevels.getCodePriority(task.getPriority()).name();
        this.bootstrap = Task.PriorityLevels.getCodePriority(task.getPriority()).getBootstrap();
    }
}
