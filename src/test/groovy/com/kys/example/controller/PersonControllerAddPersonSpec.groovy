package com.kys.example.controller

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.result.DataResponse
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Narrative

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author kody.kim
 * @since 20/01/2020
 */
@Narrative( value = """
    Person 사용자 등록 테스트 진행
"""
)
class PersonControllerAddPersonSpec extends PersonControllerConfig  {

    def "사용자 등록 요청"(){

        given:
        personService.addPerson(_) >> DataResponse.ok(1l)

        def param = MockMvcRequestBuilders.post("/person")
                .param("name", "kody.kim")
                .param("address", "서울시 강북구 수유동")
                .param("age", "32")

        when:
        def resultAction = mockMvc.perform(param)
                                .andDo(print())
        then:
        resultAction.andExpect(status().isOk())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0000.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))
        resultAction.andExpect(jsonPath('$.data').isNumber())

    }

    def "이미 등록된 사용자 요청"(){

        given:
        personService.addPerson(_) >> { throw new IllegalArgumentException("이미 등록된 사람입니다.")}

        def param = MockMvcRequestBuilders.post("/person")
                .param("name", "kody.kim")
                .param("address", "서울시 강북구 수유동")
                .param("age", "32")

        when:
        def resultAction = mockMvc.perform(param)
                                .andDo(print())

        then:
        resultAction.andExpect(status().is5xxServerError())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_S999.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_S999.getMessage()))
        resultAction.andExpect(jsonPath('$.data').doesNotExist())
    }

    def "name 필수값 누락 에러 발생 "(){

        given:
        def param = MockMvcRequestBuilders.post("/person")
                .param("name", "")
                .param("address", "서울시 강북구 수유동")
                .param("age", "32")

        when:
        def resultAction = mockMvc.perform(param)
                                .andDo(print())

        then:
        resultAction.andExpect(status().is4xxClientError())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0001.getCode()))
        resultAction.andExpect(jsonPath('$.message').value("name 은 필수 입니다."))
        resultAction.andExpect(jsonPath('$.data').doesNotExist())
    }
}
