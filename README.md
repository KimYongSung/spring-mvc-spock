# spring mvc + spock 샘플 프로젝트

현재 회사에서 spring mvc 기반으로 구축되어 있습니다. 온라인에 boot 기준으로 작성된 예제들은 spock 설정관련하여 참고하기 어려워 mvc 기반으로 샘플 프로젝트를 구성하였습니다.

## 1. maven 설정

mvc 환경은 다 구성되어 있다는 가정하에 spock 관련 설정만 추가합니다.

* spock-core 와 spock-spring 의존성 추가

```xml
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
    <version>1.3-groovy-2.5</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-spring</artifactId>
    <version>1.3-groovy-2.5</version>
    <scope>test</scope>
</dependency>
```

* byte-buddy 와 objenesis 의존성 추가

spock 에서 제공하는 mock 기능을 사용하기 위해 필요합니다.

> hibernate 를 사용하는 경우 byte-buddy가 이미 의존성에 포함되어 있으므로 제외해주세요

```xml
<dependency> <!-- enables mocking of classes (in addition to interfaces) -->
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>1.9.3</version>
    <scope>test</scope>
</dependency>
<dependency> <!-- enables mocking of classes without default constructor (together with CGLIB) -->
    <groupId>org.objenesis</groupId>
    <artifactId>objenesis</artifactId>
    <version>2.6</version>
    <scope>test</scope>
</dependency>
```

* 테스트 용 클래스패스 설정
  * maven 의 기본 디렉토리 구조는 src/test/java로 java 대신 groovy 를 설정
  * spock의 [Global Extensions](http://spockframework.org/spock/docs/1.3/extensions.html#_global_extensions)을 사용하기 위해 src/test/resources 를 설정
  
```xml
<build>
...
    <testSourceDirectory>src/test/groovy</testSourceDirectory> <!-- test 소스 디렉토리 설정 -->
    <testResources>
        <testResource>
            <directory>src/test/resources</directory>
        </testResource>
    </testResources>
...
</build>
```

* maven plugin 설정
  * maven-surefire-plugin 설정
    * maven 테스트 코드 관련 플러그인로 default로 *Test.java 클래스들을 찾아서 테스트를 실행합니다
    * spock은 선택이지만 클래스명을 Test가 아닌 Spec로 명시하여 *Spec도 include 해야 정상적으로 테스트 실해이 가능합니다.
  * gmavenplus-plugin 설정
    * groovy 를 컴파일하는 플러그인으로 spock 사용시 필수 설정입니다.
```xml
<build>
...
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.0.0-M4</version>
        <configuration>
            <argLine>${jvm.options}</argLine>
            <useFile>false</useFile>
            <includes>
                <include>**/*Test.java</include>
                <include>**/*Spec.java</include>
            </includes>
        </configuration>
    </plugin>
    
    <!-- Mandatory plugins for using Spock -->
    <plugin>
        <!-- The gmavenplus plugin is used to compile Groovy code. To learn more about this plugin,
        visit https://github.com/groovy/GMavenPlus/wiki -->
        <groupId>org.codehaus.gmavenplus</groupId>
        <artifactId></artifactId>
        <version>1.8.1</version>
        <executions>
            <execution>
                <goals>
                    <goal>compile</goal>
                    <goal>compileTests</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
...
</build>
```

## 2. spring mvc 환경에서 클래스별 설정

1. spock은 Specification을 필수적으로 상속받아야 테스트 코드 작성이 가능합니다.
2. 테스트 클래스별로 테스트 스프링 설정이 필요합니다. ( 테스트 코드는 동일합니다 )

### 2.1 spring boot 과 spring mvc 에서 spock 사용법

spring mvc 는 spring boot 과 차이점은 test 설정관련 어노테이션의 차이점입니다. 


|구분 |spring boot            | spring mvc  |
|---| --------------------- | ----------- |
|controller |@SpringBootTest  @AutoConfigureMockMvc 또는 @MockMvcTest  | @ContextConfiguration  @WebAppConfiguration |
|service | @SpringBootTest | @ContextConfiguration  @WebAppConfiguration |
|repository | @SpringBootTest | @ContextConfiguration  @WebAppConfiguration |

> 내부 소스를 확인해본건 아니지만.. spock은 spring에서 사용시 servletContext를 필수로 요구하였습니다. @WebAppConfiguration 은 필수적으로 설정이 필요합니다.

#### 2.1.1 spring mvc
```groovy
@ContextConfiguration("Spring 설정 xml or Java Config class 정보")
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonControllerConfig extends Specification {

    @SpringBean 
    SamplePersonService personService = Mock()

    @Autowired
    PersonController personController

    MockMvc mockMvc

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setControllerAdvice(new GlobalRestExceptionHandler()) // mvc 는 직접 등록해야함.
                .build()
    }
    ...
}
```

#### 2.1.2 spring boot
```groovy
@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerSpockTest extends Specification {

    @SpringBean
    private PersonService personService = Mock()

    @Autowired
    private MockMvc mockMvc // spring boot 은 내부에서 처리

    ...
}
```

### 2.1.3 spring bean mock 생성하기

spock 에서 spring bean을 mock으로 사용하는 방법은 3가지 있습니다.

1. mockito 를 사용하여 mock 생성
2. [DetachedMockFactory](http://spockframework.org/spock/docs/1.3/modules.html#_spring_module) 을 사용하여 mock 생성
3. [Annotation driven](http://spockframework.org/spock/docs/1.3/modules.html#_annotation_driven) 를 사용하여 mock 생성

1번은 spock이 아닌 mockito 사용법에 학습이 필요하여 제외하였고, 2번은 사용하는 bean 마다 매번 객체를 등록해야해서 제외하였습니다.

3번은 아래와 같이 사용 가능합니다.

```groovy
// mock
@SpringBean
private PersonService personService = Mock()

// stub
@SpringBean
private PersonService personService = Stub()

// spy
@SpringSpy
private PersonService personService
```

Mock, Stub, Spy 의 차이는 [해당 링크](https://brunch.co.kr/@tilltue/55) 참조 부탁드립니다.

## 3. Controller 테스트 코드 작성

Controller 테스트 코드는 요청은 두가지 장점이 있습니다.

첫번째, endpoint 별로 요청과 응답 파라미터의 유효성 검사를 자동화 할 수 있다. 일반적으로 운영환경에서 로그나 응답값을 눈으로 보면서 요청과 응답 파라미터 검증을 진행하는것은 상당히 비효율 적입니다. 
두번째, spring이 controller 요청 전후로 지원하는 기능들의 테스트가 가능합니다.

아래 코드 기준으로 테스트코드를 작성해보겠습니다. 

```java
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
```

```java
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handler(Exception e, WebRequest request){
        return handleExceptionInternal(e, Response.systemError(), null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * BindException 핸들링
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, Response.error(ex.getBindingResult()), headers, status, request);
    }

}
```

```java
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonAddRequest {

    @NotEmpty(message = "name 은 필수 입니다.")
    private String name;

    @NotEmpty(message = "address 는 필수 입니다.")
    private String address;

    private Integer age;

    @AssertTrue(message = "age 는 필수 값 입니다.")
    public boolean isValidAge(){
        return Objects.nonNull(age) || age > 0;
    }

    public Person toEntity(){
        return Person.builder()
                     .address(this.address)
                     .age(this.age)
                     .name(this.name)
                     .build();
    }
}
```

### 3.1 필수값 테스트

@Valid 를 사용하여 필수값을 검증하고 있고, 에러의 경우 ControllerAdvice를 사용하여 핸들링하고 있습니다. 

```groovy
def "name 필수값 누락 에러 발생 "(){

    given:
    def param = MockMvcRequestBuilders.post("/person")
            .param("name", "") // 1.
            .param("address", "서울시 강북구 수유동")
            .param("age", "32")

    when:
    def resultAction = mockMvc.perform(param)
                            .andDo(print())

    then:
    resultAction.andExpect(status().is4xxClientError()) //2.
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0001.getCode())) //3.
        resultAction.andExpect(jsonPath('$.message').value("name 은 필수 입니다."))
        resultAction.andExpect(jsonPath('$.data').doesNotExist())
}
```

1. name 파라미터를 공백으로 설정함.
2. GlobalRestExceptionHandler를 통해서 핸들링되고 400에러 발생
3. 유효성 검사 에러 json 응답 검증

### 3.2 호출한 서비스 내부에서 에러 발생 테스트

```groovy
def "이미 등록된 사용자 요청"(){

    given:
    personService.addPerson(_) >> { throw new IllegalArgumentException("이미 등록된 사람입니다.")} // 1.

    def param = MockMvcRequestBuilders.post("/person")
            .param("name", "kody.kim")
            .param("address", "서울시 강북구 수유동")
            .param("age", "32")

    when:
    def resultAction = mockMvc.perform(param)
                            .andDo(print())

    then:
    resultAction.andExpect(status().is5xxServerError()) // 2.
    resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_S999.getCode())) // 3.
    resultActionㅎ.andExpect(jsonPath('$.message').value(ErrorCode.CD_S999.getMessage()))
    resultAction.andExpect(jsonPath('$.data').doesNotExist())
}
```

1. personService.addPerson() 호출 시 IllegalArgumentException 발생하도록 Stubbing 설정
2. GlobalRestExceptionHandler 를 통해서 해당 에러가 핸들링되고 500에러 발생
3. 에러응답 결과의 유효성 검사.

> Stubbing의 자세한 사용법은 [공식사이트](http://spockframework.org/spock/docs/1.3/interaction_based_testing.html#_stubbing)를 참조해 주세요

### 3.3 정상처리 테스트

```groovy
def "사용자 등록 요청"(){

    given:
    personService.addPerson(_) >> DataResponse.ok(1l) // 1.

    def param = MockMvcRequestBuilders.post("/person")
            .param("name", "kody.kim")
            .param("address", "서울시 강북구 수유동")
            .param("age", "32")

    when:
    def resultAction = mockMvc.perform(param)
                            .andDo(print())
    then:
    resultAction.andExpect(status().isOk()) // 2.
    resultAction.andExpect(jsonPath('$.code').value (ErrorCode.CD_0000.getCode())) // 3.
    resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))
    resultAction.andExpect(jsonPath('$.data').isNumber())

}
```

1. personService.addPerson(_) 호출시 성공 응답 리턴
2. http 상태코드 200 확인
3. 성공 응답 json 검증

## 4. Service 테스트 코드 작성

일반적인 mvc 환경에서 service는 업무를 실질적으로 처리하는 기능을 담당하며, 가장 중요합니다. 특히 로직 내부에서 로컬환경에서 검증하기 힘든 경우 매번 서버에 배포 이후에 테스트를 진행해야 합니다. 이럴때 mock을 사용한 테스트 코드 작성은 Service 로직을 검증 할 수 있는 방법을 제공합니다.

아래 Service 클래스를 기준으로 테스트 코드를 작성해보겠습니다.

```java
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
    public DataResponse<Long> addPerson(PersonAddRequest request){

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
    public DataResponse<PersonDTO> findPersonByName(String name){

        Person person = personMapper.findByName(name);

        if(Objects.isNull(person))
            throw new IllegalArgumentException("사용자 정보가 없습니다.");

        return DataResponse.ok(PersonDTO.of(person));
    }
}
```

### 4.1 이미 등록된 사용자 테스트 코드 작성

```groovy
def "이미 등록된 사용자 테스트"(){

    given: "특정 name으로 조회 요청시 동일한 name 의 사용자 정보를 리턴하겠다."
    personMapper.findByName(name) >> new Person(name, address, age) // 1.

    when: "사용자 등록 요청"
    DataResponse<Long> response = personService.addPerson(request);

    then: "예외 발생 검증"
    def ex = thrown(exception) // 2.
    ex.message == _message

    where: // 3.
    name         | address           | age | request                                  || exception                        | _message
    "kody.kim"   | "서울시 강북구 수유동" | 32  | new PersonAddRequest (name, address, age)|| IllegalArgumentException.class   | "이미 등록된 사람입니다."
}
```

1. personMappe에 findByName 호출 시 Person 정보를 리턴하도록 설정
2. service 로직 기준으로 동일한 이름을 가진 사용자가 있을 경우 예외가 발생하여 검증 로직 작성
3. 테스트 용 파라미터 설정.

### 4.2 정상 처리 테스트

```groovy
def "사용자 등록 테스트"(){

    given: "특정 name 으로 조회 요청시 null을 리턴하겠다."
    personMapper.findByName(name) >> null // 1.

    when: "사용자 등록 요청"
    DataResponse<Long> response = personService.addPerson(request);

    then: "처리 결과 검증" 
    Objects.nonNull(response) 
    response.getCode() == _code
    response.getMessage() == _message
    1 * personMapper.insert(_) // 2.

    where: // 3.
    name         | address           | age | request                                  || _code                         | _message
    "kody.kim"   | "서울시 강북구 수유동" | 32  | new PersonAddRequest (name, address, age)|| ErrorCode.CD_0000.getCode()   | ErrorCode.CD_0000.getMessage()
}
```

1. personMapper에 findByName 호출 시 null 을 리턴하도록 설정
2. personMapper.insert 호출 여부 검증 로직 작성
3. 테스트 요청 파라미터 설정

> 2번에 해당하는 테스트는 Mocking 이라고 하며 자세한 사용법은 [공식사이트](http://spockframework.org/spock/docs/1.3/interaction_based_testing.html#_mocking) 를 참조 해주세요

``샘플 프로젝트에서 service 로직은 repository 관련 로직만 mock을 사용하였지만, 외부와 연동하는 로직들도 mock을 사용하여 테스트 가능합니다.`` 

## 5. repository 테스트 코드 작성 (dao 또는 mapper)

일반적으로 개발용 데이터베이스가 별도로 존재하먀 해당 데이터베이스에 모든 개발자가 접속하여 개발 및 테스트를 진행합니다. 여기서 문제가 누군가 테스트 데이터를 변경시킬 경우 테스트 매번 데이터를 다시 설정해야 합니다.

이때,  h2와 같이 EmbeddedDatabase를 사용하거나 DBUnit 같은 테스트 프레임워크를 사용하는 경우 아래와 같은 경우 ``한번 작성된 테스트 코드로 개인이 아닌 팀 단위로 로컬에서 테스트코드 실행이 가능``합니다.

샘플 프로젝트는 h2를 사용하여 테스트 코드를 작성하였습니다.

### 5.1 spring mvc 기준 EmbeddedDatabase 설정

```java
@Bean
public DataSource dataSource(){
    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScripts("classpath:sql/schema.sql") // schema 생성
            .build();
}
```

```sql
/* classpath:sql/schema.sql 파일 정보 */
CREATE TABLE person (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    age INT
);

CREATE SEQUENCE person_seq;
```

레거시 환경에서는 한번에 모든걸 다 생성하기에는 시간도 많이 들어가고 작성하다가 현기증이 발생할 수 있습니다. DBUnit이나 EmbeddedDatabase를 구성시 작성하는 service 코드나 repository 기준으로 scipt를 생성한다면 언젠가 모든 스키마를 생성할 수 있습니다.

> ORM을 사용하는 경우 데이터베이스 방언과 스키마 자동생성 기능을 통해서 entity 기준으로 사용하는 데이터베이스에 맞춰서 스키마를 생성시킬 수 있습니다.

### 5.2 Repository 코드 작성

아래 Mapper에서 findByName 메소드 테스트코드를 작성해보겠습니다. 

```java
@Mapper
public interface PersonMapper {

    ...

    /**
     * 이름으로 person 조회
     * @param name
     * @return
     */
    @Select("SELECT * FROM person WHERE name = #{name}")
    Person findByName(@Param("name") String name);

    ...
}
```

```groovy
def "이름으로 person 정보가 정상적으로 조회되야 한다."(){

    given: "테스트를 위해 person 정보 저장"
    mapper.insert(new Person(name, address, age)) // 1. 

    when: "name으로 person 정보 조회"
    Person person = mapper.findByName(_name) // 2.

    then: "person 정보 검증" // 3.
    Objects.nonNull(person)
    person.getName() == name
    person.getAddress() == address
    person.getAge() == age

    cleanup: "고객정보 삭제" // 4.
    mapper.deleteAll()

    where:
    name        | address           | age || _name
    "kody.kim"  | "서울시 강북구 수유동" | 32  || "kody.kim"
}
```

1. EmbeddedDatabase에 데이터 저장
2. 저장된 데이터 조회
3. 조회된 데이터 검증
4. 저장한 데이터 삭제 처리