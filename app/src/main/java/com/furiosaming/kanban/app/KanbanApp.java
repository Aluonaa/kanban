package com.furiosaming.kanban.app;


import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.web.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AppConfig.class})
public class KanbanApp {

    public static void main(String[] args) {
        SpringApplication.run(KanbanApp.class, args);
        Project p = new Project();

    }
}
