package com.furiosaming.kanban.service.service.impl;


import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Member;
import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.persistence.model.enums.SortFieldProject;
import com.furiosaming.kanban.persistence.repository.ProjectRepository;
import com.furiosaming.kanban.persistence.specification.ProjectSpecification;
import com.furiosaming.kanban.service.converter.filter.ProjectFilterMapper;
import com.furiosaming.kanban.service.dto.MemberDto;
import com.furiosaming.kanban.service.dto.ProjectDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.MemberService;
import com.furiosaming.kanban.service.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.furiosaming.kanban.service.converter.MemberMapper.memberDtoListUserToMemberDto;
import static com.furiosaming.kanban.service.converter.PageRequestMapper.pageRequestMap;
import static com.furiosaming.kanban.service.converter.ProjectMapper.*;
import static com.furiosaming.kanban.service.errors.Errors.*;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;
    private final UserService userService;

    public ProjectServiceImpl(UserService userService, MemberService memberService, ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.memberService = memberService;
        this.userService = userService;
    }


    @Override
    public PagingResponseDto<List<ProjectDto>> getAllProjects(PagingRequestDto<ProjectFilter> pagingRequestDto) {
        log.debug("ProjectServiceImpl getAllProjects {}", pagingRequestDto);
        PageRequest pageRequest;
        pageRequest = pageRequestMap(pagingRequestDto);
        ProjectFilter projectFilter = pagingRequestDto.getData();
        CommonFilter<SortFieldProject> commonFilter = ProjectFilterMapper.filterToCommonFilter(projectFilter);
        Page<Project> pageProject = projectRepository.findAll(ProjectSpecification.filterProject(commonFilter),
                pageRequest);
        long total = pageProject.getTotalElements();
        List<Project> allProject = pageProject.getContent();
        List<ProjectDto> allProjectDto = allProjectDtoMap(allProject);
        /**
         *  user service, создается список MemberDto,
         *  сформированный из members уже найденных проектов,
         *  позже в него мапим тех юзеров, которых получили из user service
         **/
        Set<MemberDto> memberDtoSet = new HashSet<>();
        memberDtoSet.addAll(allProjectDto.stream().map(ProjectDto::getAuthor).distinct().collect(Collectors.toList()));
        List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(userService.getUpnList(memberDtoSet));
        memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);

        return PagingResponseDto.<List<ProjectDto>>newPageBuilder().ok(Collections.singletonList(allProjectDto), total, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }


    @Override
    public BaseDataResponse<ProjectDto> getByIdProject(Long id) {
        log.debug("ProjectServiceImpl getByIdProject {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            ProjectDto projectDto = projectToDtoMap(project);
            /** Взаимодействие с user service **/
            MemberDto memberDto = projectDto.getAuthor();
            MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
            memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);
            return BaseDataResponse.<ProjectDto>newBuilder().ok(projectDto);
        } else {
            return BaseDataResponse.<ProjectDto>newBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
        }
    }

    @Override
    @Transactional
    public BaseDataResponse<ProjectDto> saveProject(ProjectDto projectDto) {
        log.debug("ProjectServiceImpl saveProject {}", projectDto);
        if (projectDto.getId() == null) {
            return createProject(projectDto);
        } else {
            return editProject(projectDto);
        }
    }

    private BaseDataResponse<ProjectDto> createProject(ProjectDto projectDto) {
        if (projectDto.getName() != null && projectDto.getAuthor() != null) {
            Member member = memberService.createOrGetUser(projectDto.getAuthor().getUpn());
            if (member == null) {
                return BaseDataResponse.<ProjectDto>newBuilder().addError(missing(MISSING_FIELD_AUTHOR)).build();
            }
            Project project = dtoToProjectMap(projectDto);
            project.setAuthor(member);
            project = projectRepository.save(project);
            projectDto.setId(project.getId());
            ProjectDto projectDtoResp = projectToDtoMap(project);

            /** Взаимодействие с user service **/
            MemberDto memberDto = projectDto.getAuthor();
            MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
            memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);
            return BaseDataResponse.<ProjectDto>newBuilder().ok(projectDtoResp);
        } else {
            return BaseDataResponse.<ProjectDto>newBuilder().addError(missing(MISSING_FIELDS)).build();
        }
    }

    private BaseDataResponse<ProjectDto> editProject(ProjectDto projectDto) {
        Optional<Project> optionalProject = projectRepository.findById(projectDto.getId());
        if (optionalProject.isPresent()) {
            if (projectDto.getName() != null) {
                Project project = optionalProject.get();
                project.setName(projectDto.getName());
                projectRepository.save(project);
                projectDto = projectToDtoMap(project);

                /** Взаимодействие с user service **/
                MemberDto memberDto = projectDto.getAuthor();
                MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
                memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);

                return BaseDataResponse.<ProjectDto>newBuilder().ok(projectDto);
            } else {
                return BaseDataResponse.<ProjectDto>newBuilder().addError(missing(MISSING_FIELD_NAME)).build();
            }
        } else {
            return BaseDataResponse.<ProjectDto>newBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
        }
    }

    @Override
    @Transactional
    public BaseDataResponse<ProjectDto> deleteProject(Long id) {
        log.debug("ProjectServiceImpl deleteProject {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setStatus(Status.DELETED);
            projectRepository.save(project);
            ProjectDto projectDto = projectToDtoMap(project);
            /** Взаимодействие с user service **/
            MemberDto memberDto = projectDto.getAuthor();
            MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
            memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);

            return BaseDataResponse.<ProjectDto>newBuilder().ok(projectDto);
        } else {
            return BaseDataResponse.<ProjectDto>newBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
        }
    }

    @Override
    public Project getProject(Long id) {
        log.debug("ProjectServiceImpl getProject {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.orElse(null);
    }

}
