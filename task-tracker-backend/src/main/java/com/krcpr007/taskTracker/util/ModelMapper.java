package com.krcpr007.taskTracker.util;

import com.krcpr007.taskTracker.model.Task;
import com.krcpr007.taskTracker.model.User;
import com.krcpr007.taskTracker.payload.TaskResponse;
import com.krcpr007.taskTracker.payload.UserSummary;

public class ModelMapper {

    public static TaskResponse mapTasktoTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setName(task.getName());
        taskResponse.setDate(task.getDate());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setComplete(task.isComplete());
        User user = task.getUser();
        UserSummary summary = new UserSummary(user.getId(), user.getUsername(), user.getName());
        taskResponse.setCreatedBy(summary);

        return taskResponse;
    }
}
