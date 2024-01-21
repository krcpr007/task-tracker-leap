package com.krcpr007.taskTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.krcpr007.taskTracker.model.Task;
import com.krcpr007.taskTracker.payload.*;
import com.krcpr007.taskTracker.repository.TaskRepository;
import com.krcpr007.taskTracker.repository.UserRepository;
import com.krcpr007.taskTracker.security.CurrentUser;
import com.krcpr007.taskTracker.security.UserPrincipal;
import com.krcpr007.taskTracker.service.TaskService;
import com.krcpr007.taskTracker.util.AppConstants;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        @Autowired
        private TaskRepository taskRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TaskService taskService;

        private static final Logger logger = LoggerFactory.getLogger(TaskController.class); // logger 

        @GetMapping
        public PagedResponse<TaskResponse> getTasks(@CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
                return taskService.getTasksCreatedBy(currentUser, page, size);
        }

        @PostMapping
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest,
                                                @CurrentUser UserPrincipal currentUser ) {
                Task task = taskService.createTask(taskRequest, currentUser.getUsername());

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{taskId}")
                        .buildAndExpand(task.getId()).toUri();

                return ResponseEntity.created(location)
                        .body(new ApiResponse(true, "Task Created Successfully"));
        }

        @PostMapping("/task/complete")
        public TaskCompleteResponse completeTask(@RequestBody TaskCompleteRequest taskCompleteRequest){
                return taskService.completeTask(taskCompleteRequest);
        }
        @PutMapping("/{taskId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> updateTask(@PathVariable Long taskId,
                                                @Valid @RequestBody TaskRequest updatedTaskRequest,
                                                @CurrentUser UserPrincipal currentUser) {
                Task updatedTask = taskService.updateTask(taskId, updatedTaskRequest, currentUser.getUsername());

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{taskId}")
                        .buildAndExpand(updatedTask.getId()).toUri();

                return ResponseEntity.created(location)
                        .body(new ApiResponse(true, "Task Updated Successfully"));
        }

        
        @DeleteMapping("/{taskId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> deleteTask(@PathVariable Long taskId, @CurrentUser UserPrincipal currentUser) {
                logger.info("TaskController called : {} {}",  currentUser.getUsername());

                taskService.deleteTask(taskId, currentUser.getUsername());
                return ResponseEntity.ok(new ApiResponse(true, "Task Deleted Successfully"));
        }


}
