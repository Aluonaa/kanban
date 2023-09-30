package com.furiosaming.kanban.service.converter.filter;

import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.enums.SortFieldTask;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.service.dto.filter.TaskFilter;
import org.springframework.util.ObjectUtils;

public class TaskFilterMapper {

    public static CommonFilter<SortFieldTask> filterToCommonFilter(TaskFilter taskFilter) {
        CommonFilter<SortFieldTask> commonFilter = new CommonFilter<>();
        if (taskFilter == null) {
            return commonFilter;
        }
        if (!ObjectUtils.isEmpty(taskFilter.getStatus())) {
            commonFilter.setStatus((taskFilter.getStatus()));
        }
        if (!ObjectUtils.isEmpty(taskFilter.getName())) {
            commonFilter.setName(taskFilter.getName());
        }
        if (!ObjectUtils.isEmpty(taskFilter.getAuthor())) {
            commonFilter.setAuthor(taskFilter.getAuthor());
        }
        if (taskFilter.getSortFieldTask() != null) {
            commonFilter.setSortField(taskFilter.getSortFieldTask());
        } else commonFilter.setSortField(SortFieldTask.STATUS);
        if (taskFilter.getSortType() != null) {
            commonFilter.setSortType(taskFilter.getSortType());
        } else commonFilter.setSortType(SortType.ASCENDING);
        return commonFilter;
    }
}
