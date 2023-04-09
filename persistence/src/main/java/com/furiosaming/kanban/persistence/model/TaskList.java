package com.furiosaming.kanban.persistence.model;

import com.furiosaming.kanban.persistence.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "task_list")
@Data
@NoArgsConstructor
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "author")
    private Member author;
    @Column(nullable = false)
    private int maxTask;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskList")
    private Set<Task> task;
}
