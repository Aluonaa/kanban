package com.furiosaming.kanban.persistence.specification;

import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.persistence.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> filterTask(CommonFilter commonFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(commonFilter.getUpn())) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("upn"), commonFilter.getUpn()));
            }
            if (!ObjectUtils.isEmpty(commonFilter.getName())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")),
                        commonFilter.getName()));
            }
            if (!ObjectUtils.isEmpty(commonFilter.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), commonFilter.getStatus()));
            }
            if (ObjectUtils.isEmpty(commonFilter.getStatus())) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), Status.ACTIVE),
                        criteriaBuilder.equal(root.get("status"), Status.COMPLETE), criteriaBuilder.equal(root.get("status"), Status.DEADLINE)));
            }
            /**STATUS**/
            /**Eсли задача выполнена, сортировать по дате выполнения, если задача в процессе - по дате создания**/
            /** этот код не работает **/
//            Expression expression;
//            expression = criteriaBuilder.selectCase().when(criteriaBuilder.equal(root.get("status"), Status.COMPLETE),
//                    root.get("finish"))
//                    .when(criteriaBuilder.equal(root.get("status"), Status.ACTIVE), root.get("dateOfCreate"))
//                    .when(criteriaBuilder.equal(root.get("status"), Status.DEADLINE), root.get("deadline"))
//            ;
            Path path;
            path = root.get(commonFilter.getSortField().toString().toLowerCase());
            if (commonFilter.getSortType() == SortType.DESCENDING) {
                query.orderBy(criteriaBuilder.desc(path));
            } else {
                query.orderBy(criteriaBuilder.asc(path));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
