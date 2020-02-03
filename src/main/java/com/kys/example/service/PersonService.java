package com.kys.example.service;

import com.kys.example.common.result.Response;
import com.kys.example.model.PersonAddRequest;

/**
 * 사용자 도메인 관련 서비스
 * @author kody.kim
 * @since 29/01/2020
 */
public interface PersonService {

    /**
     * 사용자 추가
     * @param request 사용자 정보
     * @return
     */
    Response addPerson(PersonAddRequest request);

    /**
     * 이름으로 조회
     * @param name
     * @return
     */
    Response findPersonByName(String name);
}
