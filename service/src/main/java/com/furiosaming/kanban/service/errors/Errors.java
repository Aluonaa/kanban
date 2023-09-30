package com.furiosaming.kanban.service.errors;



public class Errors {

    public static String MEMBERS_NOT_FOUND = "Участники не найдены";
    public static String ID_PROJECT_NOT_FOUND = "Проект с полученным id не найден";
    public static String ID_AUTHOR_NOT_FOUND = "Автор с полученным id не найден";
    public static String ID_EXECUTOR_NOT_FOUND = "Исполнитель с полученным id не найден";
    public static String MISSING_FIELDS = "Некоторые поля не заполнены";
    public static String MISSING_FIELD_NAME = "Поле названия не заполнено";
    public static String MISSING_FIELD_AUTHOR = "Поле автора не заполнено";
    public static String ID_TASK_LIST_NOT_FOUND = "Список задач с полученным id не найден";
    public static String ID_TASK_NOT_FOUND = "Задача с полученным id не найдена";


    public static ProgramError notFound(String message) {
        ProgramError programError = new ProgramError();
        programError.setType(CommonErrorTypes.NOT_FOUND);
        programError.setMessage(message);
        programError.setCode(null);
        return programError;
    }

    public static ProgramError missing(String message) {
        ProgramError programError = new ProgramError();
        programError.setType(CommonErrorTypes.VALIDATION);
        programError.setMessage(message);
        programError.setCode(null);
        return programError;
    }

}
