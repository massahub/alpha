package com.massa.alpha.common;

import lombok.Getter;

@Getter
public enum ResultMassage {

    SUCCESS(0, "정상 처리 되었습니다."),
    MAIL_CREATE(2,"이메일을 통해 회원가입을 완료해주세요."),
    MAIL_PWD(3,"이메일로 비밀번호가 전송되었습니다."),
    FAIL(1, "처리 실패 되었습니다."),
    NONE(-999, "알 수 없는 오류입니다."),
    FAIL_PASSWORD_CURRENT_SAME(-2, "현재 비밀번호와 이전 비밀번호가 동일합니다."),
    FAIL_PASSWORD_CONFIRM(-3, "변경 비밀번호 와 비밀번호 확인 번호가 동일하지 않습니다."),
    FAIL_MAIL_SEND(-4,"이메일 전송중 오류가 발생하였습니다."),
    FAIL_MAIL_MODE(-5,"메일 mode 오류가 발생하였습니다."),
    FAIL_TEMP_PWD(-6,"임시비밀번호 생성중 오류가 발생하였습니다."),

    FAIL_MENU_URL(-101, "URL이 일치하지 않습니다.");


    //Common
    private final int code;
    private final String message;

    ResultMassage(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
