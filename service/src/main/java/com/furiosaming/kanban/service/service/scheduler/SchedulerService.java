package com.furiosaming.kanban.service.service.scheduler;

import com.furiosaming.kanban.service.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class SchedulerService {
    private final TaskService taskService;

    public SchedulerService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "@daily")
    public void OverdueTasks() {
        taskService.updateDeadlineStatus();
    }
}

