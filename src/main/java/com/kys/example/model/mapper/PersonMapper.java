package com.kys.example.model.mapper;

import com.kys.example.model.Person;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Person 매퍼
 * @author kody.kim
 * @since 28/01/2020
 */
@Mapper
public interface PersonMapper {

    /**
     * id로 person 조회
     * @param id
     * @return
     */
    @Select("SELECT * FROM person WHERE id = #{id}")
    Person findById(@Param("id") Long id);

    /**
     * 이름으로 person 조회
     * @param name
     * @return
     */
    @Select("SELECT * FROM person WHERE name = #{name}")
    Person findByName(@Param("name") String name);

    /**
     * Person 저장
     * @param person
     * @return
     */
    @Insert("INSERT INTO PERSON (id, name, address, age) VALUES(#{id}, #{name}, #{address}, #{age})")
    @SelectKey(statement = "SELECT person_seq.nextval", keyProperty = "id", before = true, resultType = Long.class)
    Integer insert(Person person);

    /**
     * person 삭제
     * @param person
     * @return
     */
    @Delete("DELETE FROM person WHERE id = #{id}")
    Integer delete(Person person);

    /**
     * person 전체 삭제
     */
    @Delete("DELETE FROM person")
    void deleteAll();
}
