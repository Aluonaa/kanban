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
    public static String AAAAA = "ОШИБКА ТУТ";


    public static OperationError notFound(String message) {
        OperationError operationError = new OperationError();
        operationError.setType(String.valueOf(NOT_FOUND));
        operationError.setMessage(message);
        operationError.setCode(null);
        return operationError;
    }

    public static OperationError missing(String message) {
        OperationError operationError = new OperationError();
        operationError.setType(String.valueOf(VALIDATION));
        operationError.setMessage(message);
        operationError.setCode(null);
        return operationError;
    }

}
