package com.kys.example.service

import com.kys.example.common.result.DataResponse
import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.model.Person
import com.kys.example.model.PersonDTO
import com.kys.example.model.mapper.PersonMapper
import org.spockframework.spring.SpringBean
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Narrative
import spock.lang.Specification

/**
 *
 * @author kody.kim
 * @since 20/01/2020
 */
@Narrative( value = """
    Person 조회 service 테스트 
"""
)
class PersonServiceFindByNameSpec extends PersonServiceConfig {

    def "person 이름으로 조회"(){

        given: "특정 name으로 조회 요청시 동일한 name 의 사용자 정보를 리턴하겠다."
        personMapper.findByName(name) >> new Person(name, address, age)

        when: "특정 name으로 고객정보 조회"
        DataResponse<PersonDTO> response = personService.findPersonByName(name);

        then: "조회된 고객정보 검증"
        Objects.nonNull(response)
        response.getData().getName() == _name
        response.getData().getAddress() == _address
        response.getData().getAge() == _age

        where:
        name         | address           | age || _name       | _address           | _age
        "kody.kim"   | "서울시 강북구 수유동" | 32  || "kody.kim"  | "서울시 강북구 수유동"  | 32
        "kody.kim1"  | "서울시 강북구 수유동" | 32  || "kody.kim1" | "서울시 강북구 수유동"  | 32
        "kody.kim2"  | "서울시 강북구 수유동" | 32  || "kody.kim2" | "서울시 강북구 수유동"  | 32
    }

    def "등록된 사용자가 아닌 경우"(){

        given: "특정 name으로 조회 요청시 null을 리턴하겠다."
        def name = "kody.kim";
        personMapper.findByName(name) >> null

        when: "특정 name으로 고객정보 조회"
        personService.findPersonByName(name);

        then: "조회된 고객정보 검증"
        def ex = thrown(IllegalArgumentException.class)
        ex.message == "사용자 정보가 없습니다."
    }
}