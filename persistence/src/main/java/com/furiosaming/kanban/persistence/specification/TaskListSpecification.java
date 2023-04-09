package com.furiosaming.kanban.persistence.specification;


import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.persistence.model.enums.SortFieldTaskList;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.persistence.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class TaskListSpecification {
    public static Specification<TaskList> filterTaskList(CommonFilter commonFilter) {
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

            Order order;
            Expression expression;
            /**MAX_TASK**/
            /**По числу свободных задач(максимум минус кол-во уже имеющихся**/
            if (commonFilter.getSortField() == SortFieldTaskList.MAX_TASK) {
                Subquery<Long> cq = query.subquery(Long.class);
                Root<Task> taskRoot = cq.from(Task.class);
                cq.select(criteriaBuilder.count(taskRoot)).where(criteriaBuilder
                        .and(criteriaBuilder.equal(taskRoot.get("taskList"), root.get("id")), criteriaBuilder.equal(taskRoot.get("status"), Status.ACTIVE)));
                Expression<Long> expressionMaxTask = cq.getSelection();
                expression = criteriaBuilder.diff(root.get("maxTask"), expressionMaxTask);

                /**MAX_DISTINCT_AUTHOR**/
                /**По числу уникальных авторов задач в списке**/
            } else if (commonFilter.getSortField() == SortFieldTaskList.MAX_DISTINCT_AUTHOR) {
                Subquery<Long> cq = query.subquery(Long.class);
                Root<Task> taskRoot = cq.from(Task.class);
                cq.select(criteriaBuilder.countDistinct(taskRoot.get("author"))).where(criteriaBuilder
                        .and(criteriaBuilder.equal(taskRoot.get("taskList"), root.get("id")), criteriaBuilder.equal(taskRoot.get("status"), Status.ACTIVE)));
                Expression<Long> expressionMaxDistinctAuthor = cq.getSelection();
                expression = criteriaBuilder.abs(expressionMaxDistinctAuthor);

                /**NAME**/
                /**Сортировки, не предполагающие сложных запросов**/
            } else if (commonFilter.getSortField() == SortFieldTaskList.NAME) {
                expression = root.get(commonFilter.getSortField().toString().toLowerCase());
            } else {
                expression = root.get("name");
            }

            if (commonFilter.getSortType() == SortType.DESCENDING) {
                query.orderBy(criteriaBuilder.desc(expression));
            } else {
                query.orderBy(criteriaBuilder.asc(expression));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

