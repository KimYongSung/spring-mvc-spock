package com.kys.example.service;

import com.kys.example.common.result.DataResponse;
import com.kys.example.common.result.Response;
import com.kys.example.model.Person;
import com.kys.example.model.PersonAddRequest;
import com.kys.example.model.PersonDTO;
import com.kys.example.model.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author kody.kim
 * @since 28/01/2020
 */
@RequiredArgsConstructor
@Service
public class SamplePersonService implements PersonService{

    private final PersonMapper personMapper;

    /**
     * 사용자 추가
     * @param request 사용자 정보
     * @return
     */
    @Override
    public Response addPerson(PersonAddRequest request){

        Person person = personMapper.findByName(request.getName());

        if(Objects.nonNull(person)){
            throw new IllegalArgumentException("이미 등록된 사람입니다.");
        }

        Person newPerson = request.toEntity();

        personMapper.insert(newPerson);

        return DataResponse.ok(newPerson.getId());
    }

    /**
     * 이름으로 조회
     * @param name
     * @return
     */
    @Override
    public Response findPersonByName(String name){

        Person person = personMapper.findByName(name);

        if(Objects.isNull(person))
            throw new IllegalArgumentException("사용자 정보가 없습니다.");

        return DataResponse.ok(PersonDTO.of(person));
    }
}
