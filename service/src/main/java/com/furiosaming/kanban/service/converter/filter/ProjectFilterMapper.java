package com.furiosaming.kanban.service.converter.filter;

import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.enums.SortFieldProject;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;

public class ProjectFilterMapper {
    public static CommonFilter<SortFieldProject> filterToCommonFilter(ProjectFilter projectFilter) {
        CommonFilter<SortFieldProject> commonFilter = new CommonFilter<>();
        if (projectFilter == null) {
            return commonFilter;
        }
        commonFilter.setName(projectFilter.getName());
        if (projectFilter.getAuthor() != null && projectFilter.getAuthor().getUpn() != null) {
            commonFilter.setUpn(projectFilter.getAuthor().getUpn());
        }
        if (projectFilter.getSortFieldProject() != null) {
            commonFilter.setSortField(projectFilter.getSortFieldProject());
        } else {
            commonFilter.setSortField(SortFieldProject.NAME);
        }
        if (projectFilter.getSortType() != null) {
            commonFilter.setSortType(projectFilter.getSortType());
        } else {
            commonFilter.setSortType(SortType.ASCENDING);
        }
        return commonFilter;
    }
}
