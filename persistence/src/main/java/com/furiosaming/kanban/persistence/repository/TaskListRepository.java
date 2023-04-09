package com.furiosaming.kanban.persistence.repository;

import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.persistence.model.TaskList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskListRepository extends JpaRepository<TaskList, Long>, JpaSpecificationExecutor<TaskList> {
    Page<TaskList> findByProject(Project project, Pageable pageable);
}
