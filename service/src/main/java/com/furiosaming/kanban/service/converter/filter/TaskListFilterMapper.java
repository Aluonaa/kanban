package com.furiosaming.kanban.service.converter.filter;


import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.enums.SortFieldTaskList;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import org.springframework.util.ObjectUtils;

public class TaskListFilterMapper {
    public static CommonFilter<SortFieldTaskList> filterToCommonFilter(TaskListFilter taskListFilter) {
        CommonFilter<SortFieldTaskList> commonFilter = new CommonFilter<>();
        if (taskListFilter == null) {
            return commonFilter;
        }
        if(!ObjectUtils.isEmpty(taskListFilter.getName())) {
            commonFilter.setName(taskListFilter.getName());
        }
        if (taskListFilter.getMaxTask() > 0) {
            commonFilter.setMaxTask(taskListFilter.getMaxTask());
        }
        if (taskListFilter.getSortFieldTaskList() != null) {
            commonFilter.setSortField(taskListFilter.getSortFieldTaskList());
        } else {
            commonFilter.setSortField(SortFieldTaskList.NAME);
        }
        if (taskListFilter.getSortType() != null) {
            commonFilter.setSortType(taskListFilter.getSortType());
        } else {
            commonFilter.setSortType(SortType.ASCENDING);
        }
        return commonFilter;
    }

}

