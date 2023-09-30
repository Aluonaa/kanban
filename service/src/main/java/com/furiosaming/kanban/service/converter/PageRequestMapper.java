package com.furiosaming.kanban.service.converter;

import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import org.springframework.data.domain.PageRequest;

public class PageRequestMapper {
    public static PageRequest pageRequestMap(PagingRequestDto pagingRequestDto) {
        PageRequest pageRequest;
//        if (pagingRequestDto.getPage() != null && pagingRequestDto.getLimit() != null) {
            if (pagingRequestDto.getPage() >= 0) {
                if (pagingRequestDto.getLimit() > 0) {
                    pageRequest = PageRequest.of(pagingRequestDto.getPage(), pagingRequestDto.getLimit());
                } else pageRequest = PageRequest.of(pagingRequestDto.getPage(), 10);
            } else {
                if (pagingRequestDto.getLimit() > 0) {
                    pageRequest = PageRequest.of(0, pagingRequestDto.getLimit());
                } else {
                    pageRequest = PageRequest.of(0, 10);
                }
            }
        return pageRequest;
    }
}
