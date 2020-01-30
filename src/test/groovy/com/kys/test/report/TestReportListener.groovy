package com.kys.test.report

import groovy.util.logging.Slf4j
import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo

import java.time.LocalDateTime

/**
 * 테스트 결과 핸들링
 * @author kody.kim
 * @since 21/01/2020
 */
@Slf4j
class TestReportListener extends AbstractRunListener{

    private final String projectName

    private final String version

    private String className

    private String fileName

    private String pkg

    private boolean specFailed

    private boolean featureFailed

    private boolean iterationFailed

    private Throwable ex;

    TestReportListener(String version) {
        this.projectName = System.getProperty("project.name")
        this.version = version
    }

    @Override
    void beforeSpec(SpecInfo spec) {
        className = spec.getName()
        fileName = spec.getFilename()
        pkg = spec.getPackage()
    }

    @Override
    void beforeFeature(FeatureInfo feature) {
        featureFailed = false
    }

    @Override
    void beforeIteration(IterationInfo iteration) {
        iterationFailed = false
    }

    /**
     * 테스트 케이스별 수행 종료 후 호출됨.
     * @param iteration
     */
    @Override
    void afterIteration(IterationInfo iteration) {
        printResult(iteration, TestStatus.getStatus(iterationFailed))
    }

    @Override
    void afterFeature(FeatureInfo feature) {
    }

    @Override
    void afterSpec(SpecInfo spec) {
        if(spec.getFilename() != "TestConfiguration.groovy")
            printResult(spec, TestStatus.getStatus(iterationFailed))
    }

    @Override
    void error(ErrorInfo error) {

        specFailed = true

        // 테스트가 실패한 feature와 iteration 정보 확인
        FeatureInfo feature = error.getMethod().getFeature()
        IterationInfo iteration = error.getMethod().getIteration()

        if (feature != null) {
            featureFailed = true
        }

        if (iteration != null) {
            iterationFailed = true
            ex = error.getException()
        }
    }

    @Override
    void specSkipped(SpecInfo spec) {
    }

    @Override
    void featureSkipped(FeatureInfo feature) {
        printResult(feature, TestStatus.SKIP)
    }

    /**
     * 테스트 결과 출력
     * @param spec
     * @param status
     */
    private void printResult(Object spec, TestStatus status){

        String description = spec instanceof SpecInfo ? spec.getNarrative() : ((IterationInfo)spec).getName()

        TestReport report = new TestReport()
        report.setProject(projectName)
        report.setPkg(pkg)
        report.setVersion(version)
        report.setTestClass(fileName)
        report.setStatus(status)
        report.setExecuteTime(LocalDateTime.now())
        report.setThrowable(ex)
        report.setDescription(description)

        log.info(report.toString())
    }
}
