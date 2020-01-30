package com.kys.example.controller

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.result.DataResponse
import com.kys.example.model.PersonDTO
import spock.lang.Narrative
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
/**
 *
 * @author kody.kim
 * @since 29/01/2020
 */
@Narrative( value = """
    Person 사용자 등록 테스트 진행
"""
)
class PersonControllerFIndPersonSpec extends PersonControllerConfig{

    def "name 으로 person 정보 조회"(){

        given: "성공 응답 생성"
        personService.findPersonByName(_) >> DataResponse.ok(personDTO)

        when: "조회 호출"
        def resultAction = mockMvc.perform(get("/person/{name}", personDTO.getName()))

        then: "응답 검증"
        resultAction.andExpect(status().isOk())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0000.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))
        resultAction.andExpect(jsonPath('$.data').exists())
        resultAction.andExpect(jsonPath('$.data.id').value(_id))
        resultAction.andExpect(jsonPath('$.data.name').value(_name))
        resultAction.andExpect(jsonPath('$.data.address').value(_address))
        resultAction.andExpect(jsonPath('$.data.age').value(_age))

        where:
        personDTO                                              || _id | _name       | _address           | _age
        new PersonDTO(1l, "kody.kim", "서울시 강북구 수유동", 32)  || 1l  | "kody.kim"  | "서울시 강북구 수유동"  | 32
    }
}