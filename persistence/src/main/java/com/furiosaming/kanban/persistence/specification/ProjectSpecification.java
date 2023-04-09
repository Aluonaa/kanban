package com.furiosaming.kanban.persistence.specification;


import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.persistence.model.enums.SortFieldProject;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.persistence.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {
    public static Specification<Project> filterProject(CommonFilter commonFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), Status.ACTIVE), criteriaBuilder.equal(root.get("status"), Status.COMPLETE)));

            if (!ObjectUtils.isEmpty(commonFilter.getUpn())) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("upn"), commonFilter.getUpn()));
            }
            if (!ObjectUtils.isEmpty(commonFilter.getName())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")),
                        commonFilter.getName()));
            }

            Path path;
            if (commonFilter.getSortField() == SortFieldProject.AUTHOR) {
                path = root.get("author").get("upn");
            } else {
                if (commonFilter.getSortField() == null)
                    path = root.get("name");
                else {
                    path = root.get(commonFilter.getSortField().toString().toLowerCase());
                }
            }
//
            if (commonFilter.getSortType() == SortType.DESCENDING) {
                query.orderBy(criteriaBuilder.desc(path));
            } else {
                query.orderBy(criteriaBuilder.asc(path));
            }
//            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
