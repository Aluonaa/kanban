package com.furiosaming.kanban.service.service.impl;


import com.furiosaming.kanban.persistence.filters.CommonFilter;
import com.furiosaming.kanban.persistence.model.Member;
import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.persistence.model.enums.SortFieldTask;
import com.furiosaming.kanban.persistence.model.enums.Status;
import com.furiosaming.kanban.persistence.repository.TaskRepository;
import com.furiosaming.kanban.persistence.specification.TaskSpecification;
import com.furiosaming.kanban.service.converter.filter.TaskFilterMapper;
import com.furiosaming.kanban.service.dto.MemberDto;
import com.furiosaming.kanban.service.dto.TaskDto;
import com.furiosaming.kanban.service.dto.filter.TaskFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.MemberService;
import com.furiosaming.kanban.service.service.TaskListService;
import com.furiosaming.kanban.service.service.TaskService;
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
import static com.furiosaming.kanban.service.converter.TaskListMapper.taskListToDtoMap;
import static com.furiosaming.kanban.service.converter.TaskMapper.*;
import static com.furiosaming.kanban.service.errors.Errors.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MemberService memberService;
    private final TaskListService taskListService;
    private final UserService userService;

    public TaskServiceImpl(UserService userService, TaskRepository taskRepository, MemberService memberService, TaskListService taskListService) {
        this.taskRepository = taskRepository;
        this.memberService = memberService;
        this.taskListService = taskListService;
        this.userService = userService;
    }

    public void updateDeadlineStatus() {
        Date currentDate = new Date();
        List<Task> tasks = taskRepository.findByStatus(Status.ACTIVE);
        List<Task> taskDeadline = new ArrayList<>();
        tasks.forEach(task -> {
            if (currentDate.after(task.getDeadline())) {
                task.setStatus(Status.DEADLINE);
                taskDeadline.add(task);
            }
        });
        taskRepository.saveAll(taskDeadline);
    }

    @Override
    public PagingResponseDto<List<TaskDto>> getAllTasks(PagingRequestDto<TaskFilter> pagingRequestDto) {
        log.debug("TaskServiceImpl getAllTasks {}", pagingRequestDto);
        PageRequest pageRequest;
        pageRequest = pageRequestMap(pagingRequestDto);
        TaskFilter taskFilter = pagingRequestDto.getData();
        CommonFilter<SortFieldTask> commonFilter = TaskFilterMapper.filterToCommonFilter(taskFilter);
        Page<Task> pageTask = taskRepository.findAll(TaskSpecification.filterTask(commonFilter), pageRequest);
        long total = pageTask.getTotalElements();
        List<Task> allTask = pageTask.getContent();
        List<TaskDto> allTaskDto = allTaskDtoMap(allTask);
        /** user service **/
        Set<MemberDto> memberDtoSet = new HashSet<>();
        memberDtoSet.addAll(allTaskDto.stream().map(TaskDto::getAuthor).collect(Collectors.toList()));
        memberDtoSet.addAll(allTaskDto.stream().map(TaskDto::getExecutor).collect(Collectors.toList()));
        Set<String> upns = userService.getUpnList(memberDtoSet);
        List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
        memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);
        return PagingResponseDto.<List<TaskDto>>newPageBuilder().ok(Collections.singletonList(allTaskDto), total, pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    @Override
    public PagingResponseDto<List<TaskDto>> allTaskOfTaskList(Long id, PagingRequestDto<TaskListFilter> pagingRequestDto) {
        log.debug("ProjectServiceImpl allTaskListOfProject {}", pagingRequestDto);
        PageRequest pageRequest;
        pageRequest = pageRequestMap(pagingRequestDto);
        Optional<TaskList> optionalTaskList = Optional.ofNullable(taskListService.getTaskList(id));
        if (optionalTaskList.isPresent()) {
            TaskList taskList = optionalTaskList.get();
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<Task> pageAllTask = getAllTasksByTaskList(taskList, pageable);
            long total = pageAllTask.getTotalElements();
            List<Task> allTask = pageAllTask.toList();
            List<TaskDto> allTaskDto = allTaskDtoMap(allTask);
            /** Взаимодействие с user service **/
            Set<MemberDto> memberDtoSet = new HashSet<>();
            memberDtoSet.addAll(allTaskDto.stream().map(TaskDto::getAuthor).collect(Collectors.toList()));
            memberDtoSet.addAll(allTaskDto.stream().map(TaskDto::getExecutor).collect(Collectors.toList()));
            Set<String> upns = userService.getUpnList(memberDtoSet);
            List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
            memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);

            return PagingResponseDto.<List<TaskDto>>newPageBuilder().ok(Collections.singleton(allTaskDto), total, pageRequest.getPageNumber(), pageRequest.getPageSize());
        } else {
            return PagingResponseDto.<List<TaskDto>>newPageBuilder().addError(notFound(ID_PROJECT_NOT_FOUND)).build();
        }
    }

    @Override
    public BaseDataResponse<TaskDto> getByIdTask(Long id) {
        log.debug("TaskServiceImpl getByIdTask {}", id);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            TaskDto taskDto = taskToDtoMap(optionalTask.get());
            /** user service **/
            Set<MemberDto> memberDtoSet = new HashSet<>();
            memberDtoSet.add(taskDto.getAuthor());
            memberDtoSet.add(taskDto.getExecutor());
            Set<String> upns = userService.getUpnList(memberDtoSet);
            List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
            memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);
            return BaseDataResponse.<TaskDto>newBuilder().ok(taskDto);
        } else {
            return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_TASK_NOT_FOUND)).build();
        }
    }

    @Override
    @Transactional
    public BaseDataResponse<TaskDto> saveTask(TaskDto taskDto) {
        log.debug("TaskServiceImpl saveTask {}", taskDto);
        if (taskDto.getId() == null) {
            return createTask(taskDto);
        } else {
            return editTask(taskDto);
        }
    }

    private BaseDataResponse<TaskDto> createTask(TaskDto taskDto) {
        if (taskDto.getName() != null && taskDto.getDescription() != null && taskDto.getDeadline() != null
                && taskDto.getExecutor() != null && taskDto.getAuthor() != null && taskDto.getTaskListDto() != null
                && taskDto.getTaskListDto().getId() != null) {
            Member executor = memberService.createOrGetUser(taskDto.getExecutor().getUpn());
            Member author = memberService.createOrGetUser(taskDto.getAuthor().getUpn());
            TaskList taskList = taskListService.getTaskList(taskDto.getTaskListDto().getId());
            if (taskList == null) {
                return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_TASK_LIST_NOT_FOUND)).build();
            }
            if (executor == null) {
                return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_EXECUTOR_NOT_FOUND)).build();
            }
            if (author == null) {
                return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_AUTHOR_NOT_FOUND)).build();
            }
            Date currentDate = new Date();
            Task task = dtoToTaskMap(taskDto);
            task.setAuthor(author);
            task.setExecutor(executor);
            task.setTaskList(taskList);
            task.setDateOfCreate(currentDate);
            task.setDateOfUpdate(currentDate);
            taskRepository.save(task);
            taskDto.setId(task.getId());
            taskDto.setTaskListDto(taskListToDtoMap(task.getTaskList()));
            /** user service **/
            Set<MemberDto> memberDtoSet = new HashSet<>();
            memberDtoSet.add(taskDto.getAuthor());
            memberDtoSet.add(taskDto.getExecutor());
            Set<String> upns = userService.getUpnList(memberDtoSet);
            List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
            memberDtoListUserToMemberDto(new ArrayList<>(memberDtoSet), memberDtoListFromUserService);

            return BaseDataResponse.<TaskDto>newBuilder().ok(taskDto);
        }
        return BaseDataResponse.<TaskDto>newBuilder().addError(missing(MISSING_FIELDS)).build();
    }

    private BaseDataResponse<TaskDto> editTask(TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
        if (optionalTask.isPresent()) {
            if (taskDto.getName() != null && taskDto.getDescription() != null && taskDto.getDeadline() != null
                    && taskDto.getExecutor() != null && taskDto.getTaskListDto() != null) {
                Member executor = memberService.createOrGetUser(taskDto.getExecutor().getUpn());
                TaskList taskList = taskListService.getTaskList(taskDto.getTaskListDto().getId());
                if (executor == null) {
                    return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_EXECUTOR_NOT_FOUND)).build();
                }
                if (taskList == null) {
                    return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_TASK_LIST_NOT_FOUND)).build();
                }
                Date currentDate = new Date();
                Task task = optionalTask.get();
                task.setName(taskDto.getName());
                task.setDescription(taskDto.getDescription());
                task.setDeadline(taskDto.getDeadline());
                if (currentDate.before(task.getDeadline())) {
                    task.setStatus(Status.DEADLINE);
                }
                task.setExecutor(executor);
                task.setDateOfUpdate(currentDate);
                taskRepository.save(task);
                taskDto = taskToDtoMap(task);
                /** user service **/
                Set<MemberDto> memberDtoList = new HashSet<>();
                memberDtoList.add(taskDto.getAuthor());
                memberDtoList.add(taskDto.getExecutor());
                Set<String> upns = userService.getUpnList(memberDtoList);
                List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
                memberDtoListUserToMemberDto(new ArrayList<>(memberDtoList), memberDtoListFromUserService);
                return BaseDataResponse.<TaskDto>newBuilder().ok(taskDto);
            }
            return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(MISSING_FIELDS)).build();
        }
        return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_TASK_NOT_FOUND)).build();
    }

    @Override
    @Transactional
    public BaseDataResponse<TaskDto> deleteTask(Long id) {
        log.debug("TaskServiceImpl deleteTask {}", id);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(Status.DELETED);
            taskRepository.save(task);
            TaskDto taskDto = taskToDtoMap(task);
            /** user service **/
            Set<MemberDto> memberDtoList = new HashSet<>();
            memberDtoList.add(taskDto.getAuthor());
            memberDtoList.add(taskDto.getExecutor());
            Set<String> upns = userService.getUpnList(memberDtoList);
            List<MemberDto> memberDtoListFromUserService = userService.getUsersFromUserService(upns);
            memberDtoListUserToMemberDto(new ArrayList<>(memberDtoList), memberDtoListFromUserService);
            return BaseDataResponse.<TaskDto>newBuilder().ok(taskDto);
        }
        return BaseDataResponse.<TaskDto>newBuilder().addError(notFound(ID_TASK_NOT_FOUND)).build();
    }

    @Override
    public Page<Task> getAllTasksByTaskList(TaskList taskList, Pageable pageable) {
        return taskRepository.findByTaskList(taskList, pageable);
    }
}

