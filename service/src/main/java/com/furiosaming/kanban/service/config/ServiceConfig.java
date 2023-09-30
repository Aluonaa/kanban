package com.furiosaming.kanban.service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
        "com.furiosaming.kanban.service"
})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.furiosaming.kanban.persistence.repository"
})

@EntityScan(basePackages = {
        "com.furiosaming.kanban.persistence.model"
})
public class ServiceConfig {
}
