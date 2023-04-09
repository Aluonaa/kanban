package com.furiosaming.kanban.persistence.repository;

import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.persistence.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findByTaskList(TaskList taskList, Pageable pageable);

    Page<Task> findByStatus(Status status, Pageable pageable);

    List<Task> findByStatus(Status status);
}
