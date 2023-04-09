package com.furiosaming.kanban.service.service;

import com.furiosaming.kanban.persistence.model.Member;

public interface MemberService {

    Member createOrGetUser(String upn);

    // TODO: 04.07.2022
    // Member getCurrentUser();
}
