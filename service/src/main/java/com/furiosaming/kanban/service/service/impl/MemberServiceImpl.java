package com.furiosaming.kanban.service.service.impl;


import com.furiosaming.kanban.persistence.model.Member;
import com.furiosaming.kanban.persistence.repository.MemberRepository;
import com.furiosaming.kanban.service.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.furiosaming.kanban.service.converter.MemberMapper.dtoToMemberMap;


@Slf4j
@Component
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public Member createOrGetUser(String upn) {
        log.debug("MemberServiceImpl createOrGetUser {}", upn);
        Optional<Member> optionalMember = memberRepository.findByUpn(upn);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return member;
        } else {
            if (upn != null) {
                Member member = dtoToMemberMap(upn);
                memberRepository.save(member);
                return member;
            } else return null;
        }
    }

}
