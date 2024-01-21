package com.krcpr007.taskTracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.krcpr007.taskTracker.exception.BadRequestException;
import com.krcpr007.taskTracker.exception.ResourceNotFoundException;
import com.krcpr007.taskTracker.model.Task;
import com.krcpr007.taskTracker.model.User;
import com.krcpr007.taskTracker.payload.*;
import com.krcpr007.taskTracker.repository.TaskRepository;
import com.krcpr007.taskTracker.repository.UserRepository;
import com.krcpr007.taskTracker.security.CustomUserDetailsService;
import com.krcpr007.taskTracker.security.UserPrincipal;
import com.krcpr007.taskTracker.util.AppConstants;
import com.krcpr007.taskTracker.util.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public PagedResponse<TaskResponse> getAllTasks(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve tasks
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Task> tasks = taskRepository.findAll(pageable);

        if(tasks.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), tasks.getNumber(),
                    tasks.getSize(), tasks.getTotalElements(), tasks.getTotalPages(), tasks.isLast());
        }

        List<Long> taskIds = tasks.map(Task::getId).getContent();
        Map<Long, User> creatorMap = getTaskCreatorMap(tasks.getContent());
        List<TaskResponse> taskResponses = tasks.map(ModelMapper::mapTasktoTaskResponse).getContent();

        return new PagedResponse<>(taskResponses, tasks.getNumber(), tasks.getSize(), tasks.getTotalElements(), tasks.getTotalPages(), tasks.isLast());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            logger.info("Page number cannot be less than zero."+ page);
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            logger.info("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public PagedResponse<TaskResponse> getTasksCreatedBy(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);
        String username = currentUser.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all tasks created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Task> tasks = taskRepository.findByCreatedByOrderByDateDesc(user.getId(), pageable);

        if (tasks.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), tasks.getNumber(),
                    tasks.getSize(), tasks.getTotalElements(), tasks.getTotalPages(), tasks.isLast());
        }

        List<TaskResponse> taskResponses = tasks.map(ModelMapper::mapTasktoTaskResponse).getContent();

        return new PagedResponse<>(taskResponses, tasks.getNumber(),
                tasks.getSize(), tasks.getTotalElements(), tasks.getTotalPages(), tasks.isLast());
    }

    Map<Long, User> getTaskCreatorMap(List<Task> tasks) {
        // Get Task Creator details of the given list of tasks
        List<Long> creatorIds = tasks.stream()
                .map(Task::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

    public Task createTask(TaskRequest taskRequest, String username) {
        Task task = new Task();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        logger.info("TaskService called : {} {}",  taskRequest.getName());
        logger.info("TaskService called : {} {}",  taskRequest.getDate());
        logger.info("TaskService called : {} {}",  taskRequest.getDescription());

        task.setName(taskRequest.getName());
        task.setDate(taskRequest.getDate());
        task.setDescription(taskRequest.getDescription());
        task.setUser(user);

        return taskRepository.save(task);
    }

    public TaskResponse getTaskById(Long taskId, UserPrincipal currentUser) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task", "id", taskId));


        // Retrieve poll creator details
        User creator = userRepository.findById(task.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", task.getCreatedBy()));

        return ModelMapper.mapTasktoTaskResponse(task);
    }

    public TaskCompleteResponse completeTask(TaskCompleteRequest taskCompleteRequest) {
        Task task = taskRepository.findById(taskCompleteRequest.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskCompleteRequest.getTaskId()));

        task.setComplete(taskCompleteRequest.isStatus());
        taskRepository.save(task);

        TaskCompleteResponse taskCompleteResponse = new TaskCompleteResponse();
        taskCompleteResponse.setTaskId(taskCompleteRequest.getTaskId());
        taskCompleteResponse.setStatus(taskCompleteRequest.isStatus());
        taskCompleteResponse.setMessage("Successfully Completed Task");

        return taskCompleteResponse;
    }
    // update task
    public Task updateTask(Long taskId, TaskRequest updatedTaskRequest, String username) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Check if the current user is the creator of the task
        // if (!existingTask.getCreatedBy().equals(username)) {
        //     throw new BadRequestException("You don't have permission to update this task");
        // }

        // Update task properties with the new values
        existingTask.setName(updatedTaskRequest.getName());
        existingTask.setDate(updatedTaskRequest.getDate());

        // Save the updated task
        return taskRepository.save(existingTask);
    }

    // delete task
   public void deleteTask(Long taskId, String username) {
       Task task = taskRepository.findById(taskId)
               .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
               logger.info("Task: {}", task.getId());
               logger.info("Task Creator: {}", task.getCreatedBy());
               logger.info("Current User: {}", username);

    // Check if the current user is the creator of the task
    //    if (!task.getCreatedBy().equals(username)) {
    //        throw new BadRequestException("You don't have permission to delete this task");
    //    }
       logger.info("DELETE");
       taskRepository.deleteById(taskId);
   }

}
