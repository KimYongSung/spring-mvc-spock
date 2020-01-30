package com.kys.test.report


import java.time.LocalDateTime

/**
 * 테스트 결과를 저장할 클래스
 * @author kody.kim
 * @since 21/01/2020
 */
class TestReport {

    private String project;

    private String version;

    private String pkg;

    private String testClass;

    private String description;

    private TestStatus status;

    private Throwable throwable;

    private LocalDateTime executeTime;

    String getProject() {
        return project
    }

    String getVersion() {
        return version
    }

    String getPkg() {
        return pkg
    }

    String getTestClass() {
        return testClass
    }

    String getDescription() {
        return description
    }

    TestStatus getStatus() {
        return status
    }

    Throwable getThrowable() {
        return throwable
    }

    LocalDateTime getExecuteTime() {
        return executeTime
    }

    void setProject(String project) {
        this.project = project
    }

    void setVersion(String version) {
        this.version = version
    }

    void setPkg(String pkg) {
        this.pkg = pkg
    }

    void setTestClass(String testClass) {
        this.testClass = testClass
    }

    void setDescription(String description) {
        this.description = description
    }

    void setStatus(TestStatus status) {
        this.status = status
    }

    void setThrowable(Throwable throwable) {
        this.throwable = throwable
    }

    void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime
    }


    @Override
    String toString() {
        return "TestReport{" +
                "project='" + project + '\'' +
                ", version='" + version + '\'' +
                ", pkg='" + pkg + '\'' +
                ", testClass='" + testClass + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", throwable=" + throwable +
                ", executeTime=" + executeTime +
                '}';
    }
}
