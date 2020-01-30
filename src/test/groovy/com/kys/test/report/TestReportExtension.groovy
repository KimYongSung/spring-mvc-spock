package com.kys.test.report

import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

/**
 * 테스트 결과 핸들링을 위한 확장 클래스
 * @author kody.kim
 * @since 21/01/2020
 */
@Slf4j
class TestReportExtension implements IGlobalExtension{

    private String version

    TestReportExtension() {
        version = UUID.randomUUID().toString();
    }

    @Override
    void start() {
        log.info("version : {} test start", version)
    }

    @Override
    void visitSpec(SpecInfo specInfo) {
        specInfo.addListener(new TestReportListener(version));
    }

    @Override
    void stop() {
        log.info("version : {} test end", version)
    }
}
