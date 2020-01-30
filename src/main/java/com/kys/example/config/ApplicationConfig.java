package com.kys.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Application 설정 정보
 * @author kody.kim
 * @since 29/01/2020
 */
@Configuration
@Import({MyBatisConfig.class, DataSourceConfig.class})
@ComponentScan(basePackages = "com.kys.example" , excludeFilters={@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(ControllerAdvice.class)})
public class ApplicationConfig {
}
