package com.furiosaming.kanban.persistence.filters;

import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.persistence.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonFilter<T> {
    private String author;
    private int maxTask;
    private String name;
    private String upn;
    private Status status;
    private T sortField;
    private SortType sortType = SortType.ASCENDING;
}
