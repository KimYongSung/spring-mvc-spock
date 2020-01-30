package com.kys.example.service

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.result.DataResponse
import com.kys.example.model.Person
import com.kys.example.model.PersonAddRequest
import spock.lang.Narrative

/**
 * 사용자 추가 테스트
 * @author kody.kim
 * @since 30/01/2020
 */
@Narrative( value = """
    사용자 추가 테스트 
"""
)
class PersonServiceAddPersonSpec extends PersonServiceConfig{

    def "이미 등록된 사용자 테스트"(){

        given: "특정 name으로 조회 요청시 동일한 name 의 사용자 정보를 리턴하겠다."
        personMapper.findByName(name) >> new Person(name, address, age)

        when: "사용자 등록 요청"
        DataResponse<Long> response = personService.addPerson(request);

        then: "예외 발생 검증"
        def ex = thrown(exception)
        ex.message == _message

        where:
        name         | address           | age | request                                  || exception                        | _message
        "kody.kim"   | "서울시 강북구 수유동" | 32  | new PersonAddRequest (name, address, age)|| IllegalArgumentException.class   | "이미 등록된 사람입니다."
    }

    def "사용자 등록 테스트"(){

        given: "특정 name 으로 조회 요청시 null을 리턴하겠다."
        personMapper.findByName(name) >> null

        when: "사용자 등록 요청"
        DataResponse<Long> response = personService.addPerson(request);

        then: "처리 결과 검증"
        Objects.nonNull(response)
        response.getCode() == _code
        response.getMessage() == _message
        1 * personMapper.insert(_)

        where:
        name         | address           | age | request                                  || _code                         | _message
        "kody.kim"   | "서울시 강북구 수유동" | 32  | new PersonAddRequest (name, address, age)|| ErrorCode.CD_0000.getCode()   | ErrorCode.CD_0000.getMessage()
    }
}
