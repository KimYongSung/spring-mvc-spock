package com.kys.example.controller

import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.controller.exception.GlobalRestExceptionHandler
import com.kys.example.service.SamplePersonService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import spock.lang.Specification

/**
 * PersonController 공통 설정 정보
 * @author kody.kim
 * @since 29/01/2020
 */
@ContextConfiguration(classes = [ MyBatisConfig.class, DataSourceConfig.class, ApplicationConfig.class, WebMvcConfig.class])
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonControllerConfig extends Specification {

    @SpringBean
    SamplePersonService personService = Mock()

    @Autowired
    PersonController personController

    MockMvc mockMvc

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build()
    }
}
