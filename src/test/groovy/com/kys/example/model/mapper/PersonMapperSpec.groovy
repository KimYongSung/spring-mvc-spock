package com.kys.example.model.mapper

import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Narrative
import spock.lang.Specification

/**
 *
 * @author kody.kim
 * @since 20/01/2020
 */
@Narrative( value = """
PersonMapper 테스트 ( embedded h2 db 를 사용하여 테스트 진행 )
"""
)
@ContextConfiguration(classes = [ MyBatisConfig.class, DataSourceConfig.class, ApplicationConfig.class, WebMvcConfig.class])
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonMapperSpec extends Specification{

    @Autowired
    private PersonMapper mapper

    def "정상적으로 person 정보가 저장되어야 한다."(){

        given: "고객 정보 설정"
        def person = new Person("kody.kim", "서울시 강북구 수유동", 32)

        when: "저장 호출"
        mapper.insert(person)

        then: "id 값이 정상적으로 설정되었는지 확인"
        Objects.nonNull(person.getId())
        person.getId() > 0l

        cleanup: "고객정보 삭제"
        mapper.delete(person)

    }

    def "이름으로 person 정보가 정상적으로 조회되야 한다."(){

        given: "테스트를 위해 person 정보 저장"
        mapper.insert(new Person(name, address, age))

        when: "name으로 person 정보 조회"
        Person person = mapper.findByName(_name)

        then: "person 정보 검증"
        Objects.nonNull(person)
        person.getName() == name
        person.getAddress() == address
        person.getAge() == age

        cleanup: "고객정보 삭제"
        mapper.deleteAll()

        where:
        name        | address           | age || _name
        "kody.kim"  | "서울시 강북구 수유동" | 32  || "kody.kim"
    }

    def "id로 person 정보가 조회되야 한다."(){

        given: "테스트를 위한 고객 정보 저장"
        mapper.insert(person)

        when: "id로 고객정보 조회"
        Person findPerson = mapper.findById(person.getId())

        then: "저장한 고객정보와 조회된 고객정보 검증"
        findPerson.getName() == _name
        findPerson.getAddress() == _address
        findPerson.getAge() == _age

        cleanup:
        mapper.delete(person)

        where:
        person                                         || _name       | _address          | _age
        new Person("kody.kim", "서울시 강북구 수유동", 32)  || "kody.kim"  | "서울시 강북구 수유동" | 32
    }
}
