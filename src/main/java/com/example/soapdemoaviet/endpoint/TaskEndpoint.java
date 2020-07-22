package com.example.soapdemoaviet.endpoint;

import com.example.soapdemoaviet.entity.Task;
import com.example.soapdemoaviet.entity.TaskDTO;
import com.example.soapdemoaviet.service.TaskService;
import fintech.task.GetAllTaskRequest;
import fintech.task.GetAllTaskResponse;
import fintech.task.TaskInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class TaskEndpoint {
    private static final String NAMESPACE_URI = "http://www.fintech/task";

    @Autowired
    private TaskService taskService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllTaskRequest")
    @ResponsePayload
    public GetAllTaskResponse getAllTaskResponse(@RequestPayload GetAllTaskRequest request) {
        GetAllTaskResponse response = new GetAllTaskResponse();
        List<TaskInfo> taskInfoslist = new ArrayList<>();
        List<Task> taskList = taskService.getAll();
        for (Task task:taskList) {
            TaskInfo taskInfo = new TaskInfo();
            BeanUtils.copyProperties(new TaskDTO(task), taskInfo);
            taskInfoslist.add(taskInfo);
        }
        response.getTaskInfo().addAll(taskInfoslist);
        return response;
    }
}
