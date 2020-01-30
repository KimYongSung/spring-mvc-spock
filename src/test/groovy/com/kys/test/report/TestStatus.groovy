package com.kys.test.report

/**
 * 테스트 결과 상태 정보
 * @author kody.kim
 * @since 21/01/2020
 */
enum TestStatus {

    SUCCESS
    ,FAIL
    ,SKIP

    static TestStatus getStatus(boolean isFail){
        return isFail ? FAIL : SUCCESS;
    }


    @Override
    String toString() {
        return this.name();
    }
}