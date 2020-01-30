package com.kys.example.model;

import lombok.Builder;
import lombok.Getter;

/**
 * @author kody.kim
 * @since 20/01/2020
 */
@Getter
public class PersonDTO {

    private Long id;

    private String name;

    private String address;

    private Integer age;

    @Builder
    public PersonDTO(Long id, String name, String address, Integer age) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public static PersonDTO of(Person person){
        return new PersonDTO(person.getId(), person.getName(), person.getAddress(), person.getAge());
    }
}
