package com.furiosaming.kanban.persistence.model;


import com.furiosaming.kanban.persistence.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(length = 2000, nullable = false)
    private String description;
    @Column(nullable = false)
    private Date deadline;
    @Column(name = "date_of_create", nullable = false)
    private Date dateOfCreate;
    @Column(name = "date_of_update", nullable = false)
    private Date dateOfUpdate;
    @Column()
    private Date finish;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "task_list")
    private TaskList taskList;

    @ManyToOne
    @JoinColumn(name = "author")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "executor")
    private Member executor;
}
