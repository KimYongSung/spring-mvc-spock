package com.kys.example.controller;

import com.kys.example.common.result.DataResponse;
import com.kys.example.model.PersonAddRequest;
import com.kys.example.model.PersonDTO;
import com.kys.example.service.PersonService;
import com.kys.example.service.SamplePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Person 컨트롤러
 * @author kody.kim
 * @since 28/01/2020
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService service;

    @PostMapping
    public ResponseEntity<DataResponse<Long>> addPerson(@Valid PersonAddRequest request){

        DataResponse<Long> response = service.addPerson(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<DataResponse<PersonDTO>> findPersonByName(@PathVariable String name){

        DataResponse<PersonDTO> response = service.findPersonByName(name);

        return ResponseEntity.ok(response);
    }
}
