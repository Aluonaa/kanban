package com.furiosaming.kanban.service.converter;

import com.furiosaming.kanban.persistence.model.Member;
import com.furiosaming.kanban.service.dto.MemberDto;

import java.util.List;

public class MemberMapper {

    public static List<MemberDto> memberDtoListUserToMemberDto(List<MemberDto> memberDtoList, List<MemberDto> memberDtoListUserList) {
        memberDtoList.forEach(memberDto -> {
            memberDtoListUserList.forEach(memberDtoListUser -> {
                if (memberDto.equals(memberDtoListUser)) {
                    memberDto.setFirstName(memberDtoListUser.getFirstName());
                    memberDto.setMiddleName(memberDtoListUser.getMiddleName());
                    memberDto.setLastName(memberDtoListUser.getLastName());
                }
            });
        });
        return memberDtoList;
    }

    public static MemberDto memberDtoListUserToMemberDto(MemberDto memberDto, MemberDto memberDtoFromUserService) {
        if (memberDto.equals(memberDtoFromUserService)) {
            memberDto.setFirstName(memberDtoFromUserService.getFirstName());
            memberDto.setMiddleName(memberDtoFromUserService.getMiddleName());
            memberDto.setLastName(memberDtoFromUserService.getLastName());
        }

        return memberDto;
    }

    /**
     * При получении юзеров из user service
     * они имеют вид UserBean,
     * а для фронта необходим MemberDto
     **/
    public static MemberDto userBeanToMemberMapper(UserBean userBean) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUpn(userBean.getUserPrincipalName());
        memberDto.setFirstName(userBean.getFirstName());
        memberDto.setMiddleName(userBean.getMiddleName());
        memberDto.setLastName(userBean.getLastName());
        return memberDto;
    }

    public static MemberDto memberToDtoMap(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUpn(member.getUpn());
        return memberDto;
    }

    public static Member dtoToMemberMap(MemberDto memberDto) {
        Member member = new Member();
        member.setUpn(memberDto.getUpn());
        return member;
    }

    public static Member dtoToMemberMap(String upn) {
        Member member = new Member();
        member.setUpn(upn);
        return member;
    }


}
