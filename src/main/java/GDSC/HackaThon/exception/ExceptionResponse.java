package GDSC.HackaThon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 예외처리를 하기 위해서 사용하는 자바 객체
 */
@Getter @Setter
@AllArgsConstructor
public class ExceptionResponse {

    private Date timestamp;
    private String message; // 예외 메시지
    private String details; // 상세내용

}
