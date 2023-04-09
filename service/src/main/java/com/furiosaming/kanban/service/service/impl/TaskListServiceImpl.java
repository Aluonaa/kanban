package com.furiosaming.kanban.service.service.impl;




import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Member;
import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.persistence.model.enums.SortFieldTaskList;
import com.furiosaming.kanban.persistence.model.enums.Status;
import com.furiosaming.kanban.persistence.repository.TaskListRepository;
import com.furiosaming.kanban.persistence.specification.TaskListSpecification;
import com.furiosaming.kanban.service.converter.filter.TaskListFilterMapper;
import com.furiosaming.kanban.service.dto.MemberDto;
import com.furiosaming.kanban.service.dto.TaskListDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.MemberService;
import com.furiosaming.kanban.service.service.ProjectService;
import com.furiosaming.kanban.service.service.TaskListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.furiosaming.kanban.service.converter.MemberMapper.memberDtoListUserToMemberDto;
import static com.furiosaming.kanban.service.converter.PageRequestMapper.pageRequestMap;
import static com.furiosaming.kanban.service.converter.TaskListMapper.*;
import static com.furiosaming.kanban.service.errors.Errors.*;

@Slf4j
@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskListServiceImpl(UserService userService, TaskListRepository taskListRepository, MemberService memberService, ProjectService projectService) {
        this.taskListRepository = taskListRepository;
        this.memberService = memberService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PagingResponseDto<List<TaskListDto>> getAllTaskLists(PagingRequestDto<TaskListFilter> pagingRequestDto) {
        log.debug("TaskListServiceImpl getAllTaskLists {}", pagingRequestDto);
        PageRequest pageRequest;
        pageRequest = pageRequestMap(pagingRequestDto);
        TaskListFilter taskListFilter = pagingRequestDto.getData();
        CommonFilter<SortFieldTaskList> commonFilter = TaskListFilterMapper.filterToCommonFilter(taskListFilter);

        Page<TaskList> pageTaskList = taskListRepository.findAll(TaskListSpecification.filterTaskList(commonFilter),
                pageRequest);
        long total = pageTaskList.getTotalElements();
        List<TaskList> allTaskList = pageTaskList.getContent();
        List<TaskListDto> allTaskListDto = allTaskListDtoMap(allTaskList);
        /** Взаимодействие с user service **/
        Set<MemberDto> memberDtoSet = new HashSet<>();
        memberDtoSet.addAll(allTaskListDto.stream().map(TaskListDto::getAuthor).collect(Collectors.toList()));
        List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(userService.getUpnList(memberDtoSet));
        memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);

        return PagingResponseDto.<List<TaskListDto>>newPageBuilder().ok(Collections.singletonList(allTaskListDto), total, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }


    @Override
    public PagingResponseDto<List<TaskListDto>> allTaskListOfProject(Long id, PagingRequestDto<ProjectFilter> pagingRequestDto) {
        log.debug("ProjectServiceImpl allTaskListOfProject {}", pagingRequestDto);
        PageRequest pageRequest;
        pageRequest = pageRequestMap(pagingRequestDto);
        Optional<Project> optionalProject = Optional.ofNullable(projectService.getProject(id));
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<TaskList> pageAllTaskList = getTaskListsByProject(project, pageable);
            long total = pageAllTaskList.getTotalElements();
            List<TaskList> allTaskList = pageAllTaskList.toList();
            List<TaskListDto> taskListDto = allTaskListDtoMap(allTaskList);
            /** Взаимодействие с user service **/
            Set<MemberDto> memberDtoSet = new HashSet<>();
            memberDtoSet.addAll(taskListDto.stream().map(TaskListDto::getAuthor).collect(Collectors.toList()));
            List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(userService.getUpnList(memberDtoSet));
            memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);
            return PagingResponseDto.<List<TaskListDto>>newPageBuilder().ok(Collections.singleton(taskListDto), total, pageRequest.getPageNumber(), pageRequest.getPageSize());
        } else {
            return PagingResponseDto.<List<TaskListDto>>newPageBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
        }
    }


    @Override
    public BaseDataResponse<TaskListDto> getByIdTaskList(Long id) {
        log.debug("TaskListServiceImpl getByIdTaskList {}", id);
        Optional<TaskList> optionalTaskList = taskListRepository.findById(id);
        if (optionalTaskList.isPresent()) {
            TaskListDto taskListDto = taskListToDtoMap(optionalTaskList.get());
            /** Взаимодействие с user service **/
            MemberDto memberDto = taskListDto.getAuthor();
            MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
            memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);

            return BaseDataResponse.<TaskListDto>newBuilder().ok(taskListDto);
        } else {
            return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(ID_TASK_LIST_NOT_FOUND)).build();
        }
    }

    @Override
    @Transactional
    public BaseDataResponse<TaskListDto> saveTaskList(TaskListDto taskListDto) {
        log.debug("TaskListServiceImpl saveTaskList {}", taskListDto);
        if (taskListDto.getId() == null) {
            return createTaskList(taskListDto);
        } else {
            return editTaskList(taskListDto);
        }

    }

    private BaseDataResponse<TaskListDto> createTaskList(TaskListDto taskListDto) {
        if (taskListDto.getName() != null && taskListDto.getMaxTask() > 0 && taskListDto.getAuthor() != null && taskListDto.getProject() != null && taskListDto.getProject().getId() != null) {
            Member member = memberService.createOrGetUser(taskListDto.getAuthor().getUpn());
            Project project = projectService.getProject(taskListDto.getProject().getId());
            if (project == null) {
                return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
            }
            if (member == null) {
                return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(ID_AUTHOR_NOT_FOUND)).build();
            }
            TaskList taskList = dtoToTaskListMap(taskListDto);
            taskList.setAuthor(member);
            taskList.setProject(project);
            taskList = taskListRepository.save(taskList);
            taskListDto.setId(taskList.getId());
            /** Взаимодействие с user service **/
            MemberDto memberDto = taskListDto.getAuthor();
            MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
            memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);
            if (memberDtoFromUserService == null) {
                taskListDto.getAuthor().setFirstName(null);
                taskListDto.getAuthor().setMiddleName(null);
                taskListDto.getAuthor().setLastName(null);
            }

            return BaseDataResponse.<TaskListDto>newBuilder().ok(taskListDto);
        }
        return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(MISSING_FIELDS)).build();
    }

    private BaseDataResponse<TaskListDto> editTaskList(TaskListDto taskListDto) {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(taskListDto.getId());
        if (optionalTaskList.isPresent()) {
            if (taskListDto.getName() != null && taskListDto.getMaxTask() > 0) {
                TaskList taskList = optionalTaskList.get();
                taskList.setName(taskListDto.getName());
                taskList.setMaxTask(taskListDto.getMaxTask());
                taskListRepository.save(taskList);
                taskListDto = taskListToDtoMap(taskList);
                /** Взаимодействие с user service **/
                MemberDto memberDto = taskListDto.getAuthor();
                MemberDto memberDtoFromUserService = userService.getUserFromUserService(memberDto.getUpn());
                memberDtoListUserToMemberDto(memberDto, memberDtoFromUserService);
                return BaseDataResponse.<TaskListDto>newBuilder().ok(taskListDto);
            } else return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(MISSING_FIELDS)).build();
        }
        return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(ID_TASK_LIST_NOT_FOUND)).build();
    }


    @Override
    @Transactional
    public BaseDataResponse<TaskListDto> deleteTaskList(Long id) {
        log.debug("TaskListServiceImpl deleteTaskList{}", id);
        Optional<TaskList> optionalTaskList = taskListRepository.findById(id);
        if (optionalTaskList.isPresent()) {
            TaskList taskList = optionalTaskList.get();
            taskList.setStatus(Status.DELETED);
            taskListRepository.save(taskList);
            TaskListDto taskListDto = taskListToDtoMap(taskList);

            return BaseDataResponse.<TaskListDto>newBuilder().ok(taskListDto);
        } else return BaseDataResponse.<TaskListDto>newBuilder().addError(notFound(ID_TASK_LIST_NOT_FOUND)).build();
    }

    @Override
    public TaskList getTaskList(Long id) {
        log.debug("TaskListServiceImpl getTaskList{}", id);
        Optional<TaskList> optionalTaskList = taskListRepository.findById(id);
        if (optionalTaskList.isPresent()) {
            TaskList taskList = optionalTaskList.get();
            return taskList;
        } else return null;
    }

    @Override
    public Page<TaskList> getTaskListsByProject(Project project, Pageable pageable) {
        Page<TaskList> pageAllTaskList = taskListRepository.findByProject(project, pageable);
        return pageAllTaskList;
    }
}