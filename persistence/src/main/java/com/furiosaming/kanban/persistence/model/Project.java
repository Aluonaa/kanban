package com.furiosaming.kanban.persistence.model;

import com.furiosaming.kanban.persistence.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @Column(length = 255)
    private String appId;

    @ManyToOne
    @JoinColumn(name = "author")
    private Member author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<TaskList> taskList;
}
